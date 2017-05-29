import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Created by User on 29.05.2017.
 */
public class Main extends JFrame implements Runnable{
  private static Socket connection;
  private static ObjectOutputStream output;
  private static ObjectInputStream input;

  public static void main(String[] args){
    new Thread(new Main("Test")).start();
    new Thread(new Server()).start();
  }

  public Main(String name){
    super(name);
    setLayout(null);
    setSize(900, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    setLocationRelativeTo(null);

    final JTextField text = new JTextField("");
    final JLabel label = new JLabel("0");
    final JButton buttonPlus = new JButton("Summary");
    final JButton buttonMinus = new JButton("Difference");

    text.setBounds(200, 100, 500, 100);
    add(text);

    label.setBounds(200, 200, 500, 100);
    add(label);

    buttonPlus.setBounds(200, 300, 250, 100);
    buttonPlus.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (e.getSource()==buttonPlus){
          int summ = Integer.valueOf(label.getText())+Integer.valueOf(text.getText());
          label.setText(String.valueOf(summ));
          sendData(summ);
        }
      }
    });
    add(buttonPlus);

    buttonMinus.setBounds(450, 300, 250, 100);
    buttonMinus.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (e.getSource()==buttonMinus){
          int difference = Integer.valueOf(label.getText())-Integer.valueOf(text.getText());
          label.setText(String.valueOf(difference));
          sendData(difference);
        }
      }
    });
    add(buttonMinus);
  }

  @Override
  public void run() {
    try {
      while (true){
        connection = new Socket(InetAddress.getByName("127.0.0.1"), 5678);
        output = new ObjectOutputStream(connection.getOutputStream());
        input = new ObjectInputStream(connection.getInputStream());
        JOptionPane.showMessageDialog(null, input.readObject());
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  private static void sendData(Object obj){
    try {
      output.flush();
      output.writeObject(obj);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
