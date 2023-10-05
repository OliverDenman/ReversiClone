package reversi;

import java.util.ArrayList;

public class ReversiController implements IController{

    IModel model;
    IView view;

    int width;
    int height;

    int black;
    int white;

    ArrayList<String> positonsToTake = new ArrayList<String>();

    @Override
    public void initialise(IModel model, IView view){
        this.model = model;
        this.view = view;
        this.width = model.getBoardWidth();
        this.height = model.getBoardHeight();
    }

    @Override
    public void startup(){
        model.setFinished(false);
        model.setPlayer(1);
        this.positonsToTake.clear();
        for (int ii = 0; ii < width; ii++)
            for (int jj = 0 ; jj < height; jj++)
                model.setBoardContents(ii, jj, 0);

        model.setBoardContents(3,3,1);
        model.setBoardContents(4,4,1);
        model.setBoardContents(3,4,2);
        model.setBoardContents(4,3,2);


        update();
        view.refreshView();
    }

    @Override
    public void update(){
        countLeft();
        if(validMoves(1) == 0 && validMoves(2) == 0){
            model.setFinished(true);
            if (white > black){
                view.feedbackToUser(1,"White won. White " + white + " to Black " + black + ". Reset the game to replay.");
                view.feedbackToUser(2,"White won. White " + white + " to Black " + black + ". Reset the game to replay.");
            }else if(black > white){
                view.feedbackToUser(1,"Black won. Black " + black + " to White " + white + ". Reset the game to replay.");
                view.feedbackToUser(2,"Black won. Black " + black + " to White " + white + ". Reset the game to replay.");
            }else if(black == white){
                view.feedbackToUser(1,"Draw. Both players ended with "+ black + " pieces. Reset the game to replay.");
                view.feedbackToUser(2,"Draw. Both players ended with "+ black + " pieces. Reset the game to replay.");
            }

            return;
        }
        if (model.getPlayer() == 1){
            if(validMoves(1) == 0 && validMoves(2) != 0){
                model.setFinished(true);
                update();
                return;
            }else{
                view.feedbackToUser(2,"Black player – not your turn");
                view.feedbackToUser(1,"White player – choose where to put your piece");
            }
        }else{
            if(validMoves(2) == 0 && validMoves(1) != 0){
                model.setFinished(true);
                update();
                return;
            }else{
                view.feedbackToUser(1,"White player – not your turn");
                view.feedbackToUser(2,"Black player – choose where to put your piece");
            }
        }
        if(model.hasFinished() != true){
            boolean finished = true;
            for (int x = 0; x < model.getBoardWidth(); x++)
                for (int y = 0; y < model.getBoardHeight(); y++)
                    if (model.getBoardContents(x, y) == 0)
                        finished = false;
            model.setFinished(finished);
            view.refreshView();
        }else{
            return;
        }


    }

    public void countLeft(){
        int playerOneScore = 0;
        int playerTwoScore = 0;
        for(int ii = 0; ii < width; ii++){
            for(int jj = 0; jj < height; jj++){
                if(model.getBoardContents(ii,jj) == 1){
                    playerOneScore++;
                }else if(model.getBoardContents(ii,jj) == 2){
                    playerTwoScore++;
                }
            }
        }
        black = playerTwoScore;
        white = playerOneScore;
    }

    @Override
    public void squareSelected(int player, int x, int y){
        if (model.hasFinished())
        {
            return;
        }
        //Only make move if valid
        if(checkValidMove(x,y,player)){
            model.setBoardContents(x,y,player);
            for(int ii = 0; ii < positonsToTake.size(); ii++){
                int posX = Character.getNumericValue(positonsToTake.get(ii).charAt(0));
                int posY = Character.getNumericValue(positonsToTake.get(ii).charAt(1));
                model.setBoardContents(posX, posY, player);
            }
            if(model.getPlayer() == 1){
                model.setPlayer(2);
            }else{
                model.setPlayer(1);
            }
            this.positonsToTake.clear();
            update();
            view.refreshView();
        }

    }

    @Override
    public void doAutomatedMove(int player){
        ArrayList<String> largetPosMove = new ArrayList<String>();
        ArrayList<String> largetPosMoveTemp = new ArrayList<String>();
        int xPos = 0;
        int yPos = 0;
        for(int ii = 0; ii < width; ii++){
            for(int jj = 0; jj < height; jj++){
                if(model.getBoardContents(ii,jj) != 0){
                    continue;
                }
                ArrayList<String> hozPosLarge = searchHorizontal(ii,jj,player);
                ArrayList<String> vertPosLarge = searchVertical(ii,jj,player);
                ArrayList<String> diagPosLarge = searchDiagonal(ii,jj,player);

                for(int kk = 0; kk < hozPosLarge.size(); kk++){
                    largetPosMoveTemp.add(hozPosLarge.get(kk));
                }
                for(int kk = 0; kk < vertPosLarge.size(); kk++){
                    largetPosMoveTemp.add(vertPosLarge.get(kk));
                }
                for(int kk = 0; kk < diagPosLarge.size(); kk++){
                    largetPosMoveTemp.add(diagPosLarge.get(kk));
                }

                if(largetPosMoveTemp.size() > largetPosMove.size()){
                    largetPosMove = largetPosMoveTemp;
                    xPos = ii;
                    yPos = jj;
                }
                largetPosMoveTemp.clear();
            }
        }
        squareSelected(player,xPos,yPos);
        largetPosMoveTemp.clear();
        largetPosMove.clear();
        view.refreshView();

    }

    public int validMoves(int player){
        ArrayList<String> largetPosMoveTemp = new ArrayList<String>();
        int count = 0;
        for(int ii = 0; ii < width; ii++){
            for(int jj = 0; jj < height; jj++){
                if(model.getBoardContents(ii,jj) != 0){
                    continue;
                }
                ArrayList<String> hozPosLarge = searchHorizontal(ii,jj,player);
                ArrayList<String> vertPosLarge = searchVertical(ii,jj,player);
                ArrayList<String> diagPosLarge = searchDiagonal(ii,jj,player);

                for(int kk = 0; kk < hozPosLarge.size(); kk++){
                    largetPosMoveTemp.add(hozPosLarge.get(kk));
                }
                for(int kk = 0; kk < vertPosLarge.size(); kk++){
                    largetPosMoveTemp.add(vertPosLarge.get(kk));
                }
                for(int kk = 0; kk < diagPosLarge.size(); kk++){
                    largetPosMoveTemp.add(diagPosLarge.get(kk));
                }

                if(largetPosMoveTemp.size() != 0){
                    count++;
                }
                largetPosMoveTemp.clear();
            }
        }

        return count;

    }

    public boolean checkValidMove(int x, int y, int player){
        if(model.getBoardContents(x,y) != 0){
            return false;
        }
        if(model.getPlayer() != player){
            view.feedbackToUser(player,"It is not your turn!");
            return false;
        }
        ArrayList<String> hozPos = searchHorizontal(x,y,player);
        ArrayList<String> vertPos = searchVertical(x,y,player);
        ArrayList<String> diagPos = searchDiagonal(x,y,player);

        for(int ii = 0; ii < hozPos.size(); ii++){
            this.positonsToTake.add(hozPos.get(ii));
        }
        for(int ii = 0; ii < vertPos.size(); ii++){
            this.positonsToTake.add(vertPos.get(ii));
        }
        for(int ii = 0; ii < diagPos.size(); ii++){
            this.positonsToTake.add(diagPos.get(ii));
        }
        if(positonsToTake.size() == 0){
            return false;
        }else{
            return true;
        }
    }

    public ArrayList<String> searchHorizontal(int x, int y, int player){
        int startXpos,startYpos,startXneg,startYneg;
        startXpos = startXneg = x;
        startYpos = startYneg = y;
        boolean validLeft = false;
        boolean validRight = false;
        ArrayList<String> capPos = new ArrayList<String>();
        ArrayList<String> capNeg = new ArrayList<String>();

        while(startXpos < (width-1)){
            startXpos++;
        if(model.getBoardContents(startXpos,startYpos) == 0){
            capPos.clear();
            break;
        }else if(model.getBoardContents(startXpos,startYpos) == player){
            validLeft = true;
            break;
        }else if(startXpos != (width-1)){
            capPos.add("" + startXpos + "" + startYpos);
            }
        }

        if(!validLeft){
            capPos.clear();
        }

        while(startXneg > 0){
            startXneg--;
        if(model.getBoardContents(startXneg,startYneg) == 0){
            capNeg.clear();
            break;
        }else if(model.getBoardContents(startXneg,startYneg) == player){
            validRight = true;
            break;
        }else if(startXneg != 0 ){
            capNeg.add("" + startXneg + "" + startYneg);
            }
        }

        if(!validRight){
            capNeg.clear();
        }

        for(int ii = 0; ii < capNeg.size(); ii++){
            capPos.add(capNeg.get(ii));
        }
        return capPos;
    }

    public ArrayList<String> searchVertical(int x, int y, int player){
        int startXpos,startYpos,startXneg,startYneg;
        startXpos = startXneg = x;
        startYpos = startYneg = y;
        boolean validUp = false;
        boolean validDown = false;
        ArrayList<String> capPos = new ArrayList<String>();
        ArrayList<String> capNeg = new ArrayList<String>();

        while(startYpos < (width-1)){
            startYpos++;
        if(model.getBoardContents(startXpos,startYpos) == 0){
            capPos.clear();
            break;
        }else if(model.getBoardContents(startXpos,startYpos) == player){
            validUp = true;
            break;
        }else if(startYpos != (height-1)){
            capPos.add("" + startXpos + "" + startYpos);
            }
        }

        if(!validUp){
            capPos.clear();
        }

        while(startYneg > 0){
            startYneg--;
        if(model.getBoardContents(startXneg,startYneg) == 0){
            capNeg.clear();
            break;
        }else if(model.getBoardContents(startXneg,startYneg) == player){
            validDown = true;
            break;
        }else if(startYneg != 0){
            capNeg.add("" + startXneg + "" + startYneg);
            }
        }

        if(!validDown){
            capNeg.clear();
        }

        for(int ii = 0; ii < capNeg.size(); ii++){
            capPos.add(capNeg.get(ii));
        }

        return capPos;
    }

    public ArrayList<String> searchDiagonal(int x, int y, int player){
        ArrayList<String> capPosLeft = new ArrayList<String>();
        ArrayList<String> capPosRight = new ArrayList<String>();
        ArrayList<String> capNegRight = new ArrayList<String>();
        ArrayList<String> capNegLeft = new ArrayList<String>();
        boolean validPosLeft,validPosRight,validNegLeft,validNegRight;
        validPosLeft = validPosRight = validNegLeft = validNegRight = false;
        int xPosLeft,xPosRight,yPosLeft,yPosRight,xNegLeft,xNegRight,yNegLeft,yNegRight;
        xNegLeft = xNegRight = xPosLeft = xPosRight = x;
        yPosLeft = yPosRight = yNegLeft = yNegRight = y;

        while(xPosRight < (width-1) && yPosRight > 0){
            xPosRight++;yPosRight--;
            if(model.getBoardContents(xPosRight,yPosRight) == 0){
                capPosRight.clear();
                break;
            }else if(model.getBoardContents(xPosRight,yPosRight) == player){
                validPosRight = true;
                break;
            }else if(xPosRight != (width-1) || yPosRight != 0){
                capPosRight.add("" + xPosRight + "" + yPosRight);
            }
        }

        if(!validPosRight){
            capPosRight.clear();
        }

        while(xPosLeft > 0 && yPosLeft > 0){
            xPosLeft--;yPosLeft--;
            if(model.getBoardContents(xPosLeft,yPosLeft) == 0){
                capPosLeft.clear();
                break;
            }else if(model.getBoardContents(xPosLeft,yPosLeft) == player){
                validPosLeft = true;
                break;
            }else if(xPosLeft != 0 || yPosLeft != 0){
                capPosLeft.add("" + xPosLeft + "" + yPosLeft);
            }
        }

        if(!validPosLeft){
            capPosLeft.clear();
        }

        while(xNegRight < (width-1) && yNegRight < (height-1)){
            xNegRight++;yNegRight++;
            if(model.getBoardContents(xNegRight,yNegRight) == 0){
                capNegRight.clear();
                break;
            }else if(model.getBoardContents(xNegRight,yNegRight) == player){
                validNegRight = true;
                break;
            }else if(xNegRight != (width-1) || yNegRight != (height-1)){
                capNegRight.add("" + xNegRight + "" + yNegRight);
            }
        }

        if(!validNegRight){
            capNegRight.clear();
        }

        while(xNegLeft > 0 && yNegLeft < (height-1)){
            xNegLeft--;yNegLeft++;
            if(model.getBoardContents(xNegLeft,yNegLeft) == 0){
                capNegLeft.clear();
                break;
            }else if(model.getBoardContents(xNegLeft,yNegLeft) == player){
                validNegLeft = true;
                break;
            }else if(xNegLeft != 0 || yNegLeft != (height-1)){
                capNegLeft.add("" + xNegLeft + "" + yNegLeft);
            }
        }

        if(!validNegLeft){
            capNegLeft.clear();
        }

        for(int ii = 0; ii < capPosLeft.size(); ii++){
            capPosRight.add(capPosLeft.get(ii));
        }

        for(int ii = 0; ii < capNegRight.size(); ii++){
            capPosRight.add(capNegRight.get(ii));
        }

        for(int ii = 0; ii < capNegLeft.size(); ii++){
            capPosRight.add(capNegLeft.get(ii));
        }

        return capPosRight;
    }

}
