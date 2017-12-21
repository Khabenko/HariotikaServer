package Net;


import Domain.Arena;
import com.google.gson.Gson;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.net.URISyntaxException;


@WebServlet("/getAvatar/")
    public class Controller extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("image/jpeg");
        //парсим id картинки из запроса
        String imageId = req.getParameter("name");
        //Тут вы получаете  Entity вашей картинки, одно из полей у которого является массивом байт
        ServletContext contex = getServletContext();
       // String path = contex.getRealPath("WEB-INF\\classes\\avatars\\"+imageId+".png");

        String path = "C:\\Avatars\\"+imageId+".png";
        File f = new File(path); // тестовая картинка 400*300 24 bit (400*300*3=360000)
        BufferedImage img = ImageIO.read(f);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", baos);
        byte[] bytes = baos.toByteArray();


        resp.setContentLength((int) bytes.length);
        // получаете поток для своих нужд
        ServletOutputStream outStream = resp.getOutputStream();
        // отсылаете картинку на клиента
        outStream.write(bytes);
        // закрываете поток
        outStream.close();









    }



}
