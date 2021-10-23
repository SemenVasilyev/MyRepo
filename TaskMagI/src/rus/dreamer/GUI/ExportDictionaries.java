package rus.dreamer.GUI;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;

public class ExportDictionaries extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField tExportWay;
    private JButton buttonFolder;

    public ExportDictionaries() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonFolder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onFolder();
            }
        });

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
// add your code here
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    private void onFolder() {
        // Вызываем диалоговое окно выбора файлов
        JFileChooser fileChooser = new JFileChooser(".");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int ret =  fileChooser.showDialog(null, "ОК");
            if (ret == JFileChooser.APPROVE_OPTION){ // Если диалоговое окно завершилось успешно
                File file  = fileChooser.getSelectedFile();
                tExportWay.setText(file.getPath());
            }

    }

    public static void main(String[] args) {
        ExportDictionaries dialog = new ExportDictionaries();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
