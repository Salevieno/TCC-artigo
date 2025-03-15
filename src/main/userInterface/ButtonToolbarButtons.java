package main.userInterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class ButtonToolbarButtons extends JButton
{

    private static final Color bgColor ;
    private static final List<JButton> all = new ArrayList<>();

    static
    {
        bgColor = Menus.palette[1];
    }

    public ButtonToolbarButtons(String text)
    {
        super(text);
        setToolTipText(text);
        setIcon(new ImageIcon("./Icons/Tb1B" + text + ".png"));
        setFocusable(false);
        setHorizontalAlignment(SwingConstants.CENTER);
        setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        setVerticalAlignment(0);
        setHorizontalAlignment(0);
        setBackground(bgColor);
        setPreferredSize(new Dimension(32, 32));
        setMargin(new Insets(0, 0, 0, 0));
        all.add(this);
    }

    public static List<JButton> getAll()
    {
        return all;
    }
}
