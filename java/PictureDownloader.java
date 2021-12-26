import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class PictureDownloader {
    public static void main(String[] args) throws IOException {

        BufferedReader buf = new BufferedReader(
                new InputStreamReader(System.in)
        );
        System.out.println("作者:小東   |   Discord:XiaoDong#0907");
        System.out.println("只是個小程式，已開源。沒事別亂拆");
        System.out.println("");
        System.out.println("請輸入檔案下載存放路徑:");

        String dirName = buf.readLine();



        download(buf,dirName);



    }

    static void download(BufferedReader buf,String dirName) throws IOException {
        System.out.println("請輸入關鍵字:");

        String question = buf.readLine();

        String url = "https://www.google.com/search?q=" + question + "&tbm=isch&sxsrf=AOaemvKwdK-h5cqjHsK5_Ro15xSmWrwUNw%3A1640509601038&source=hp&biw=1280&bih=577&ei=oDDIYfe0PPuKr7wP28eu-Ac&iflsig=ALs-wAMAAAAAYcg-sfSfoFPnaVndAgiuxystxgsL0fqg&oq=&gs_lcp=CgNpbWcQARgBMgoIIxDvAxDqAhAnMgoIIxDvAxDqAhAnMgoIIxDvAxDqAhAnMgoIIxDvAxDqAhAnMgoIIxDvAxDqAhAnMgoIIxDvAxDqAhAnMgoIIxDvAxDqAhAnMgoIIxDvAxDqAhAnMgoIIxDvAxDqAhAnMgoIIxDvAxDqAhAnUABYAGCqR2gBcAB4AIABAIgBAJIBAJgBAKoBC2d3cy13aXotaW1nsAEK&sclient=img";

        Set<String> list = new HashSet<>();

        Document doc = Jsoup.
                connect(url).get();
        String[] imgs = doc.getElementsByTag("img").toString().split(">");

        for (String str : Arrays.stream(imgs).toList()) {
            if (str.contains("https:")) {
                String[] split = str.split("\\s");

                for (String string : Arrays.stream(split).toList()) {
                    if (string.contains("data-src")) {
                        list.add(string.replace("data-src=", "").replace("\"", ""));
                    }
                }
            }
        }

        System.out.println("搜尋到" + list.size() + "張照片\n");

        System.out.println("輸入所需的張數:");

        String countStr = buf.readLine();

        int count = 0;

        if(countStr.chars().allMatch( Character::isDigit )){
            count = Integer.parseInt(countStr);
        }else{
            System.out.println("輸入錯誤，請重新輸入");
            download(buf, dirName);
        }

        if (count > list.size()) {
            count = list.size();
            System.out.println("輸入過大，將下載" + list.size() + "張照片");
        }
        int n = 1;
        for (String picUrl : list) {



            try {
                saveFileFromUrlWithJavaIO(
                        dirName + "\\" + question + n +".png", picUrl);
                System.out.println("下載進度:" + n + "/" + count);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            n++;
            if(n > count){
                System.out.println("下載完成!");
                break;
            }
        }

        System.out.println("是否再次下載?(Y/N)");
        String again = buf.readLine();

        if(again.equalsIgnoreCase("Y")){
            download(buf,dirName);
        }else{
            System.out.println("結束程式，感謝您的使用");
        }
    }

    public static void saveFileFromUrlWithJavaIO(String fileName, String fileUrl)
            throws IOException {
        BufferedInputStream in = null;
        FileOutputStream fout = null;
        try {
            in = new BufferedInputStream(new URL(fileUrl).openStream());
            fout = new FileOutputStream(fileName);
            byte data[] = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1) {
                fout.write(data, 0, count);
            }
        } finally {
            if (in != null)
                in.close();
            if (fout != null)
                fout.close();
        }
    }

}