package rus.dreamer.GUI;

import rus.dreamer.Logic.Initiator;
import rus.dreamer.Logic.Organ;
import rus.dreamer.Logic.Podrazd;
import rus.dreamer.Logic.paragraph.Paragraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class OrganChoise extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList lAddreses;
    private Object parent;
    private JFrame mainFrame;
    private MainWindow mainWindow;

    public OrganChoise(Object parent) {
        this.parent = parent;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        super.setTitle("Орган Инициатора");
        super.setMinimumSize(new Dimension(300, 200));

        // Ищем родительский Фрейм для того чтобы взять с него размеры
        Frame[] frames = JFrame.getFrames();
        //this.mainFrame = (JFrame)frames[2];
        for (Frame frame : frames) {
            if (frame instanceof JFrame && frame.getTitle().equals("MainWindow")) {
                this.mainFrame = (JFrame) frame;
            }
        }

        Window[] windows = Window.getWindows();
        mainWindow = (MainWindow) windows[0];

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

// create List of Organs
        ArrayList<Organ> organList = new ArrayList<Organ>();
        organList = Organ.getOrganList();
        Object[] orgArray = organList.toArray();
        lAddreses.setListData(orgArray);

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
        if (!lAddreses.isSelectionEmpty()) {
            mainWindow.tbInitiator.getModel().setValueAt(lAddreses.getSelectedValue().toString(), 0, 1);
            if (parent instanceof Paragraph) {
                ((Paragraph) parent).setAddress(lAddreses.getSelectedValue().toString());
            }
            if (parent instanceof Initiator) {
                ((Initiator) parent).setOrgan(lAddreses.getSelectedValue().toString());
            }

            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Выберете любой вариант из предложенных в списке!", "Предупреждение", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    public void create() {

        OrganChoise dialog = new OrganChoise(parent);
        dialog.pack();


        Object o = dialog.getParent();
        dialog.setLocation((mainFrame.getX() + mainFrame.getWidth() / 2) - dialog.getWidth() / 2, (mainFrame.getY() + mainFrame.getHeight() / 2) - dialog.getHeight() / 2); // Распологаем наш диалог по центру основного окна
        dialog.setVisible(true);

    }


}
