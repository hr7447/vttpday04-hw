package fortuneCookie;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws NumberFormatException, IOException {

        String portNumber = "";
        String dirPath = "";
        String fileName = "";

        if (args.length > 0) {
            portNumber = args[0];
            dirPath = args[1];
            fileName = args[2];
        } else {
            System.err.println("Invalid number of arguments exepected");
            System.exit(0);
        }

        File newDirectory = new File(dirPath);
        if (!newDirectory.exists()) {
            newDirectory.mkdir();
        }

        //Read and print cookies
        Cookie c = new Cookie();
        c.readCookieFile(dirPath + File.separator + fileName);
        //c.printCookies();

        ServerSocket ss = new ServerSocket(Integer.parseInt(portNumber));
        Socket s = ss.accept();
        System.out.printf("\r\nWebsocket server started on port... %s\r\n", portNumber);

        try {
            InputStream is = s.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            DataInputStream dis = new DataInputStream(bis);
            String messageReceived = "";

            OutputStream os = s.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(os);
            DataOutputStream dos = new DataOutputStream(bos);

            while (!messageReceived.toLowerCase().equals("quit")) {
                
                System.out.println("Waiting for client input...");
                messageReceived = dis.readUTF(dis);

                //Pick a random cookie
                String retrievedCookie = c.getRandomCookie();

                //Put it to the DataOutputStream to send back to client
                dos.writeUTF(retrievedCookie);
                dos.flush();
            }

            dos.close();
            bos.close();
            os.close();

            dis.close();
            bis.close();
            is.close();

        } catch (Exception ex) {
            System.err.println(ex.toString());

        } finally {

        }

    }

}