package springtraining;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Logger {

    private FileChannel channel;
    private GUI gui;

    public Logger() {
        channel = null;
        gui = null;
    }

    public void init() {
        //Получаем канал на запись в файл
        try {
            channel = new FileOutputStream("log.txt").getChannel();
        } catch (FileNotFoundException e) {
            channel = null;
        }
    }

    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public void toLog(String logElement) {
        if (channel == null) return;
        //Записываем сообщение в файл
        try {
            if (gui != null) {
                gui.print(logElement);
            }
            channel.write(ByteBuffer.wrap((logElement + "\n").getBytes()));
        } catch (IOException e) {
            return;
        }
    }

    public void close() {
        try {
            channel.close();
            if (gui != null) {
                gui.print("Канал вывода в файл " + (channel.isOpen() ? "открыт" : "закрыт"));
            }
        } catch (IOException e) {
            return;
        }
    }

}
