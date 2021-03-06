package com.example.alexander.mytest2;

import android.os.Environment;
import android.util.Log;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Created by ssd on 16/3/21.
 *
 */

public class FileReceive implements Runnable
{
    private Socket client;
    private String[] fileNamePool;

    /**
     Constructs a handler.
     @param client the incoming socket
     */
    public FileReceive(Socket client, String[] fileNamePool)
    {
        this.client = client;
        this.fileNamePool = fileNamePool;
    }

    boolean inFileNamePool(String filename){
        for(String nameInPool: fileNamePool){
            if (filename.equals(nameInPool)){
                return true;
            }
        }
        return false;
    }

    public void run()
    {
        try
        {
            InputStream inputstream = client.getInputStream();
   //get file name
            String filename = new DataInputStream(inputstream).readUTF();

            if(inFileNamePool(filename))
            {
                client.close();
            }
            else
            {
                //get file data
                final File f = new File(
                        Environment.getExternalStorageDirectory() + "/WIFIP2P/"
                                +"/Date/" +filename);

                File dirs = new File(f.getParent());
                if (!dirs.exists())
                    dirs.mkdirs();
                f.createNewFile();

                Log.d(MainActivity.TAG, "server: copying files " + f.toString());

                FileListFragment.copyFile(inputstream, new FileOutputStream(f));

                client.close();
            }

        }
        catch (IOException e)
        {
            Log.e(MainActivity.TAG, e.getMessage());
        }


    }

}
