package reversi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUIView implements IView{

    IModel model;
    IController controller;

    JFrame frame1 = new JFrame();
    JFrame frame2 = new JFrame();
    JPanel board1 = new JPanel();
    JPanel board2 = new JPanel();
    JLabel message1 = new JLabel();
    JLabel message2 = new JLabel();
    JPanel p1Panel = new JPanel();
    JPanel p2Panel = new JPanel();
    JPanel p1Button = new JPanel();
    JPanel p2Button = new JPanel();

    myButton gridBut1[][];
    myButton gridBut2[][];

    int width;
    int height;

    @Override
    public void initialise(IModel model, IController controller){

        this.controller = controller;
        this.model = model;

        this.width = model.getBoardWidth();
        this.height = model.getBoardHeight();

        //OnClose
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Titles
        frame1.setTitle("Reversi - Player 1");
        frame2.setTitle("Reversi - Player 2");

        //Positon
        frame1.setLocationRelativeTo(null);
        frame2.setLocationRelativeTo(null);

        //Set board size
        frame1.setPreferredSize(new Dimension(500,510));
        frame2.setPreferredSize(new Dimension(500,510));

        //Add BorderLayout
        p1Panel.setLayout(new BorderLayout());
        p2Panel.setLayout(new BorderLayout());

        //Fonts
        message1.setFont(new Font("Arial", Font.BOLD, 10));
        message2.setFont(new Font("Arial", Font.BOLD, 10));

        //Label
        message1.setText("Player 1 - Initial text goes here");
        p1Panel.add(message1,BorderLayout.NORTH);
        message2.setText("Player 2 - Initial text goes here");
        p2Panel.add(message2,BorderLayout.NORTH);

        //ButtonGrid
        p1Button.setLayout(new GridLayout(2,1));
        p2Button.setLayout(new GridLayout(2,1));

        //BoardGrid
        board1.setLayout(new GridLayout(height,width));
        board2.setLayout(new GridLayout(height,width));
        board1.setBackground(Color.GREEN);
        board2.setBackground(Color.GREEN);

        //Buttons
        JButton butAI1 = new JButton("Greedy AI (Play White)");
        butAI1.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {controller.doAutomatedMove(1);}});

        JButton butAI2 = new JButton("Greedy AI (Play Black)");
        butAI2.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {controller.doAutomatedMove(2);}});

        JButton butRE1 = new JButton("Restart");
        butRE1.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {controller.startup();}});

        JButton butRE2 = new JButton("Restart");
        butRE2.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) {controller.startup();}});

        //game grid

        this.gridBut1 = new myButton[width][height];
        this.gridBut2 = new myButton[width][height];

        for(int ii = 0; ii < width; ii++){
            for(int jj = 0; jj < height; jj++){
                myButton button = new myButton();
                button.xPos = (jj);
                button.yPos = (ii);
                button.player = 1;
                button.setBackground(Color.GREEN);
                button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                button.addActionListener(new gridListener());
                gridBut1[ii][jj] = button;
                board1.add(gridBut1[ii][jj]);
            }
        }

        for(int ii = width; ii > 0; ii--){
            for(int jj = height; jj > 0; jj--){
                myButton button = new myButton();
                button.xPos = (jj-1);
                button.yPos = (ii-1);
                button.player = 2;
                button.setBackground(Color.GREEN);
                button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                button.addActionListener(new gridListener());
                gridBut2[ii-1][jj-1] = button;
                board2.add(gridBut2[ii-1][jj-1]);
            }
        }

        //Add Buttons to Button Panel
        p1Button.add(butAI1);
        p1Button.add(butRE1);
        p2Button.add(butAI2);
        p2Button.add(butRE2);

        //Add boards to panels at centre pos
        p1Panel.add(board1, BorderLayout.CENTER);
        p2Panel.add(board2, BorderLayout.CENTER);

        //Add Panels to frame
        frame1.add(p1Panel);
        frame2.add(p2Panel);
        frame1.add(p1Button, BorderLayout.SOUTH);
        frame2.add(p2Button, BorderLayout.SOUTH);


        //Display
        frame1.pack();
        frame1.setVisible(true);
        frame2.pack();
        frame2.setVisible(true);

    }

    @Override
    public void refreshView(){
        for(int ii = 0; ii < width; ii++){
            for(int jj = 0; jj < height;jj++){
                myButton button1 = gridBut1[jj][ii];
                myButton button2 = gridBut2[jj][ii];
                if (model.getBoardContents(ii,jj) == 1){
                    button1.setPlayerOne();
                    button2.setPlayerOne();
                }else if (model.getBoardContents(ii,jj) == 2){
                    button1.setPlayerTwo();
                    button2.setPlayerTwo();
                }else{
                    button1.setPlayerNone();
                    button2.setPlayerNone();
                }
            }
        }

        frame1.repaint();
        frame2.repaint();
    }

    @Override
    public void feedbackToUser(int player, String message){
        if (player == 1)
            message1.setText(message);
        else if ( player == 2 )
            message2.setText(message);
    }

    private class gridListener implements ActionListener{
            public void actionPerformed(ActionEvent e){
                myButton clickedButton = (myButton) e.getSource();
                controller.squareSelected(clickedButton.player, clickedButton.xPos, clickedButton.yPos);
            }
        }

    public class myButton extends JButton{
        int xPos;
        int yPos;
        int player;
        int select;

        public void setPlayerOne(){
            this.select = 1;
        }

        public void setPlayerTwo(){
            this.select = 2;
        }

        public void setPlayerNone(){
            this.select = 0;

        }

        @Override
        public void paintComponent(Graphics g){
        	int size = Math.min(getWidth(), getHeight()) - 10;
            int xPos = (getWidth() - size) / 2;
            int yPos = (getHeight() - size) / 2;
            if(this.select == 1){
                super.paintComponent(g);

                g.setColor(Color.WHITE);
                g.fillOval(xPos, yPos, size, size);

                g.setColor(Color.BLACK);
                g.drawOval(xPos, yPos, size, size);

            }else if(this.select == 2){
                super.paintComponent(g);

                g.setColor(Color.BLACK);
                g.fillOval(xPos, yPos, size, size);

                g.setColor(Color.WHITE);
                g.drawOval(xPos, yPos, size, size);
            }else if(this.select == 0) {
                super.paintComponent(g);
            }
        }
        
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(50, 50);
        }
    }

}


