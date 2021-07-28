package com.weiwu.ql.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.provider.Telephony.Mms.Part.CHARSET;

/**
 *  
 * author : 马伟东
 * email : maweidong693@163.com
 * date : 2021/4/28 13:22 
 */
public class UploadUtil {
    private OkHttpClient okHttpClient;
    private UploadUtil(){
        okHttpClient = new OkHttpClient();
    }
    /**
     * 使用静态内部类的方式实现单例模式
     */
    private static class UploadUtilInstance{
        private static final UploadUtil INSTANCE = new UploadUtil();
    }
    public static UploadUtil getInstance(){
        return UploadUtilInstance.INSTANCE;
    }

    /**
     * @param url   服务器地址
     * @param file  所要上传的文件
     * @return      响应结果
     * @throws IOException
     */
    public ResponseBody upload(String url, File file) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(MediaType.parse("multipart/form-data"), file))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful())
            throw new IOException("Unexpected code " + response);
        return response.body();
    }


    public String uploadFile(File file,String requestURL)
    {
        String TAG = "uploadFile";
        String PREFIX = "--";
        String LINE_END = "\r\n";
        String BOUNDARY =  UUID.randomUUID().toString();  //随机生成边界

        try {
            URL url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(30 * 1000); //30秒连接超时
            connection.setReadTimeout(30 * 1000);   //30秒读取超时
            connection.setDoInput(true);  //允许文件输入流
            connection.setDoOutput(true); //允许文件输出流
            connection.setUseCaches(false);  //不允许使用缓存
            connection.setRequestMethod("POST");  //请求方式为POST
            connection.setRequestProperty("Charset", "utf-8");  //设置编码为utf-8
            connection.setRequestProperty("connection", "keep-alive"); //保持连接
            connection.setRequestProperty("Content-Type", "multipart/form-data" + ";boundary=" + BOUNDARY); //特别注意：Content-Type必须为multipart/form-data

            //如果传入的文件路径不为空的话，则读取文件并上传
            if(file!=null)
            {
                //读取图片进行压缩
                //如果不需要压缩的话直接读取文件则可 InputStream is = new FileInputStream(file);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Bitmap bitmap = BitmapFactory.decodeFile(file.toString());
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos); //0-100 100为不压缩
                InputStream is = new ByteArrayInputStream(baos.toByteArray());

                OutputStream outputSteam = connection.getOutputStream();
                DataOutputStream dos = new DataOutputStream(outputSteam);
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);

                //特别注意
                //name是服务器端需要key;filename是文件的名字（包括后缀）
                sb.append("Content-Disposition: form-data; name=\"upload!\"; filename=\""+file.getName()+"\""+LINE_END);
                sb.append("Content-Type: application/octet-stream; charset="+CHARSET+LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());

                byte[] bytes = new byte[1024];
                int len = 0;
                while((len=is.read(bytes))!=-1)
                {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();

                //获取返回码，根据返回码做相应处理
                int res = connection.getResponseCode();
                Log.d(TAG, "response code:"+res);
                if(connection.getResponseCode() == 200)
                {
                    BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while((line = input.readLine()) != null)
                    {
                        result.append(line).append("\n");
                    }
                    Log.i(TAG, result.toString());
                    return result.toString();
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
