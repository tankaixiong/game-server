package tank.http.work;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tank.http.common.ContentType;
import tank.http.common.UploadFile;
import tank.msg.common.ThreadPoolManager;
import tank.msg.utils.JsonUtil;

import java.io.IOException;
import java.util.List;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/4/5
 * @Version: 1.0
 * @Description: netty 支持HTTP 请求（GET,POST），支持文件上传
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE); //Disk

    private Logger logger = LoggerFactory.getLogger(HttpServerHandler.class);

    private IHttpDispatcher httpDispatcher;

    static {
        DiskFileUpload.deleteOnExitTemporaryFile = true;
        DiskFileUpload.baseDirectory = null;
    }

    public HttpServerHandler(IHttpDispatcher httpDispatcher) {
        this.httpDispatcher = httpDispatcher;
    }

    public void setHttpDispatcher(IHttpDispatcher httpDispatcher) {
        this.httpDispatcher = httpDispatcher;
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) throws Exception {

        if (!fullHttpRequest.decoderResult().isSuccess()) {
            sendMsg(ctx, HttpResponseStatus.BAD_REQUEST, "text/html;charset=UTF-8", "请求无法解析");
            return;
        }

        if (fullHttpRequest.uri().equalsIgnoreCase("/favicon.ico")) {
            logger.info("忽略/favicon.ico");
            ctx.write(new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.EMPTY_BUFFER));
            return;
        }
        //sendMsg(ctx, "<h1>it's work!</h1> \r\n");

        final HttpRequest request = new HttpRequest();

        if (fullHttpRequest.method() == HttpMethod.GET) {
            request.setHttpMethod(tank.http.common.HttpMethod.GET);
            String uri = fullHttpRequest.uri();


            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(uri);

            request.setUrl(queryStringDecoder.path());
            request.setUri(queryStringDecoder.uri());

            request.addParams(queryStringDecoder.parameters());


        } else if (fullHttpRequest.method() == HttpMethod.POST) {

            request.setHttpMethod(tank.http.common.HttpMethod.POST);

            HttpPostRequestDecoder postRequestDecoder = new HttpPostRequestDecoder(factory, fullHttpRequest);
            //postRequestDecoder.offer(fullHttpRequest);
            //boolean readingChunks =HttpUtil.isTransferEncodingChunked(fullHttpRequest);
            //logger.info("readingChunks={}",readingChunks);
            logger.info("isMultipart:{}", postRequestDecoder.isMultipart());

            int index = fullHttpRequest.uri().indexOf("?");
            String url = fullHttpRequest.uri();

            if (index != -1) {
                url = fullHttpRequest.uri().substring(0, index);
            }
            request.setUrl(url);
            request.setUri(fullHttpRequest.uri());


            List<InterfaceHttpData> httpDataList = postRequestDecoder.getBodyHttpDatas();
            for (InterfaceHttpData data : httpDataList) {


                if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                    Attribute attribute = (Attribute) data;
                    String value = null;
                    try {
                        value = attribute.getValue();

                    } catch (IOException e) {
                        logger.error("{}", e);
                    } finally {
                        data.release();
                    }

                    request.addParam(attribute.getName(), value);

                } else if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload) {

                    FileUpload fileUpload = (FileUpload) data;
                    if (fileUpload.isCompleted()) {

//                        byte[] fileByte = IOUtils.toByteArray(new FileInputStream(fileUpload.getFile()));
//                        IOUtils.write(fileByte, new FileOutputStream(new File("G:\\" + fileUpload.getFilename())));

//                        byte[] fileByte = FileUtils.readFileToByteArray(fileUpload.getFile());
//                        FileUtils.writeByteArrayToFile(new File("G:\\" + fileUpload.getFilename()), fileByte);

                        UploadFile uploadFile = new UploadFile();
                        uploadFile.setOrgFileName(fileUpload.getFilename());
                        uploadFile.setFile(fileUpload.getFile());

                        request.addFile(fileUpload.getName(), uploadFile);
                    }

                    //data.release(); //TODO:这里没有主动释放

                }
            }
        }


        ThreadPoolManager.getExecutor().submit(new Runnable() {
            @Override
            public void run() {
                HttpResponse response = null;
                try {
                    response = httpDispatcher.execute(request);

                    if (response.getContentType().equals(ContentType.APPLICATION_JSON)) {
                        String jsonStr = JsonUtil.toJson(response.getData());
                        sendMsg(ctx, HttpResponseStatus.OK, response.getContentType(), jsonStr);
                    } else {
                        sendMsg(ctx, HttpResponseStatus.OK, ContentType.TEXT_HTML, String.valueOf(response.getData()));
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                    sendMsg(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR, "text/html;charset=UTF-8", "服务器繁忙!");

                } finally {
                }
            }
        });


    }


    private void sendMsg(ChannelHandlerContext ctx, HttpResponseStatus status, String contextType, String content) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                status
                //,Unpooled.copiedBuffer(content, CharsetUtil.UTF_8)
        );
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, contextType);
        response.content().writeBytes(content.getBytes(CharsetUtil.UTF_8));
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
