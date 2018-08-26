/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sanmokuFx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

    class SaveMasu implements Serializable{
       Integer x;
       Integer  y;
       String koma;
     SaveMasu(Integer  x, Integer  y, String koma){
         this.x = x;
         this.y = y;
         this.koma = koma;
     }  
       
    }


/**
 *
 * @author SC
 */
public class FXMLDocumentController implements Initializable  {
    
    
    
    String KURO = "●";
    String SIRO = "〇";
    String KARA = "　";
    boolean win = false;
    boolean draw = false;
    String collar = SIRO;
    int i;// = Integer.parseInt(id.substring(1, 2));
    int j;// = Integer.parseInt(id.substring(2, 3));
    int fileNum;

    Label[][] masu = new Label[3][3];
    List<SaveMasu>  saveData = new ArrayList<>();
    List<SaveMasu> loadData = new ArrayList<>();

    File file1 = new File("Sanmoku1.ser");
    File file2 = new File("Sanmoku2.ser");
    File fileNow1 = new File("Now1.ser");
    File fileNow2 = new File("Now2.ser");
    
    boolean next = true;
    int count = 0;//////////////////////
    boolean saveSw = false;
    boolean loadSw = false;

    @FXML 
    private Button saveButton; 
    @FXML
    private Button loadButton;
    @FXML
    private Button reset;
    @FXML
    private Button data1;
    @FXML
    private Button data2;
    @FXML
    private Button nextButton;
    @FXML
    Label b00,b01,b02,b10,b11,b12,b20,b21,b22;
    @FXML
    Label msg;
        
    @FXML
    private void button1Action () throws IOException{
        fileNum = 1;
        
        if (saveSw == true) {
            save();
        }else if (loadSw == true) {
            load();
        }
    }
    
    @FXML
    private void button2Action () throws IOException{
         fileNum = 2;
        
        if (saveSw == true) {
            save();
        }else if (loadSw == true) {
            load();
        }
    }
    
    @FXML
    private void nextButton(){
        if(count<=8){
            
        SaveMasu sm = loadData.get(count);
           masu[sm.x][sm.y].setText(sm.koma);
           count++;
            System.out.println("count is " + count);
            if (count == 9) {
                nextButton.setVisible(false);
                msg.setText("");
            }
        }
    }
    
    @FXML
    private void changeSave(){
        loadSw = false;
        msg.setText("SaveMode");
        saveSw = true;
    }
   
    @FXML
    private void changeLoad(){
        saveSw = false;
        msg.setText("LoadMode");
        loadSw = true;
    }
  
    @FXML
    private void resetAll(ActionEvent event) {
        System.out.println("resetします");
        msg.setText("");
        win = false;
        draw = false;
        nextButton.setVisible(false);
        count = 0;
        resetMasu();
    }
    
    private void save() throws IOException{
        try {
            
            //buttonメソッドで指定したfileNumで切り替える
            ObjectOutputStream oos = new ObjectOutputStream
                                        (new FileOutputStream("Sanmoku" + fileNum + ".ser"));
            oos.writeObject(saveData);
            oos.close();
            
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String now = sdf.format(date);
            
            ObjectOutputStream oosNow = new ObjectOutputStream
                                        (new FileOutputStream("Now" + fileNum + ".ser"));
            oosNow.writeObject(now);
            oosNow.close();
            if (fileNum == 1) {
                data1.setText(now);
            }else if(fileNum == 2){
                data2.setText(now);
            }
            msg.setText("");
            
            System.out.println("Save Compleat!");
            
            saveData.clear();
            saveSw = false;
            
        } catch (FileNotFoundException ex) {  }
    }
    
    private void load() throws IOException{
        try {
            loadData.clear();
            ObjectInputStream ois = new ObjectInputStream
                                    (new FileInputStream("Sanmoku" + fileNum + ".ser"));
            loadData = (List<SaveMasu>)ois.readObject();
            ois.close();
            

            System.out.println("List is " + loadData.isEmpty());
            for (SaveMasu sm : loadData) {            
                System.out.println("x = " + sm.x + " : y = " + sm.y + " : koma = " + sm.koma);
            }
            
            System.out.println("Load Compleat!");
            msg.setText("");
            loadSw = true;
            count = 0;
            nextButton.setVisible(true);
            msg.setText("Please Push NextButton");
            resetMasu();

        } catch (FileNotFoundException | ClassNotFoundException ex) { }
    }
     
    private void resetMasu(){
        for (int k = 0; k < 3; k++) {
            for (int l = 0; l < 3; l++) {
                masu[k][l].setText(KARA);
            }
      }
    }
    
    @FXML
    private void clickAction( MouseEvent event ) {
        
        Label lb;
        String id;
        lb = (Label)event.getSource();
        id = lb.getId();
        int i = Integer.parseInt( id.substring(1,2) );
        int j = Integer.parseInt( id.substring(2,3) );

        System.out.println( id + "クリック");
        System.out.println( " i = " + i);
        System.out.println( " j = " + j);
        
        msg.setText("");
               
        if(!masu[i][j].getText().equals(KARA)){
            msg.setText("そこは打てません！！");
            return;
        
        }else if(win == false || draw == false){
            masu[i][j].setText(KURO);
            saveData.add(new SaveMasu(i, j, KURO));        
        }
        
        check();
        draw();
        
        
        if (draw == true) {
            System.out.println("Draw");
            msg.setText("Draw");
            return;
            
        }else if(win == true){
            System.out.println("GameOver");
            msg.setText("GameOver");
            return;
        }                    
        
        comp();
        check();
        draw();
        
        if (draw == true) {
            System.out.println("Draw");
            msg.setText("Draw");
            return;
          
        }else if(win == true){
            System.out.println("GameOver");
            msg.setText("GameOver");
            return;        
        }
    }  
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       masu[0][0] = b00; masu[0][1] = b01; masu[0][2] = b02;
       masu[1][0] = b10; masu[1][1] = b11; masu[1][2] = b12;
       masu[2][0] = b20; masu[2][1] = b21; masu[2][2] = b22;
       
       for(Label[] masu1 : masu){
           for(Label masu2 : masu1){
               masu2.setText(KARA);
           }
       }
        try {    
            if (file1.exists()) {
                ObjectInputStream oisNow1 = new ObjectInputStream(new FileInputStream("Now1.ser"));
                
                try {
                    data1.setText((String)oisNow1.readObject());
                
                } catch (ClassNotFoundException ex) {}
            
                oisNow1.close();
            }
        ///////////////////////////////////////////////////////////
        
        ObjectInputStream oisNow2 = new ObjectInputStream(new FileInputStream("Now2.ser"));
            if (file2.exists()) {
                data2.setText((String)oisNow2.readObject());
            }
        } catch (IOException | ClassNotFoundException e) {}
        
        nextButton.setVisible(false);

    }    
     

//////
    void comp(){
    
    System.out.println("コンピュータ"); 

    collar = SIRO;
    int k = 0;

    ///攻撃＆防御（横）

    for (k = 0; k < 2; k++) {
         for (i = 0; i < 3; i++) {
         ///●空● 
          if (masu[i][0].getText().equals(collar) && masu[i][2].getText().equals(collar)) {
                if (masu[i][1].getText().equals(KARA)) {
                    masu[i][1].setText(SIRO);
                     saveData.add(new SaveMasu(i, 1, SIRO));
                    return;
               }
         ///●●空
            }else if(masu[i][0].getText().equals(collar) && masu[i][1].getText().equals(collar)){
                if (masu[i][2].getText().equals(KARA)) {
                   masu[i][2].setText(SIRO);
                   saveData.add(new SaveMasu(i, 2, SIRO));
                   return;
                }
        ///空●●
            }else if(masu[i][1].getText().equals(collar) && masu[i][2].getText().equals(collar)){
                if (masu[i][0].getText().equals(KARA)) {
                   masu[i][0].setText(SIRO);
                     saveData.add(new SaveMasu(i, 0, SIRO));
                   return;
                }
            }
        }
         collar = KURO;
    }
    
    ///攻撃＆防御（縦）
        collar = SIRO;

    for (k = 0; k < 2; k++) {
         for (j = 0; j < 3; j++) {
         ///●
         ///空
         ///●
          if (masu[0][j].getText().equals(collar) && masu[2][j].getText().equals(collar)) {
                if (masu[1][j].getText().equals(KARA)) {
                    masu[1][j].setText(SIRO);
                     saveData.add(new SaveMasu(1, j, SIRO));
                    return;
               }
        ///●
        ///●
        ///空
            }else if(masu[0][j].getText().equals(collar) && masu[1][j].getText().equals(collar)){
                if (masu[2][j].getText().equals(KARA)) {
                   masu[2][j].setText(SIRO);
                     saveData.add(new SaveMasu(2, j, SIRO));
                   return;
                }
        ///空
        ///●
        ///●
            }else if(masu[1][j].getText().equals(collar) && masu[2][j].getText().equals(collar)){
                if (masu[0][j].getText().equals(KARA)) {
                   masu[0][j].setText(SIRO);
                     saveData.add(new SaveMasu(0, j, SIRO));
                   return;
                }
            }
        }
         collar = KURO;
    }
   
    ///攻撃＆防御（斜）
    collar = SIRO;

    for (k = 0; k < 2; k++) {
        if (masu[0][0].getText().equals(collar) && masu[1][1].getText().equals(collar)) {
               //●
               //　●
               //　　空
               if(masu[2][2].getText().equals(KARA)){
               masu[2][2].setText(SIRO);
               saveData.add(new SaveMasu(2, 2, SIRO));
               return;
               }
               
           }else if(masu[1][1].getText().equals(collar) && masu[2][2].getText().equals(collar)){
               //空 
               //　●
               //　　●
               if(masu[0][0].getText().equals(KARA)){
               masu[0][0].setText(SIRO);
               saveData.add(new SaveMasu(0, 0, SIRO));
               return;
               }
               
           }else if(masu[0][0].getText().equals(collar) && masu[2][2].getText().equals(collar)){
               //●
               //　空
               //　　●
               if(masu[1][1].getText().equals(KARA)){
               masu[1][1].setText(SIRO);
                 saveData.add(new SaveMasu(1, 1, SIRO));
               return;
               }
               
           }else if(masu[0][2].getText().equals(collar) && masu[1][1].getText().equals(collar)){
               //　　●
               //　●
               //空
               if(masu[2][0].getText().equals(KARA)){
               masu[2][0].setText(SIRO);
               saveData.add(new SaveMasu(2, 0, SIRO));
               return;
               }
               
           }else if(masu[1][1].getText().equals(collar) && masu[2][0].getText().equals(collar)){
               //　　空
               //　●
               //●
               if(masu[0][2].getText().equals(KARA)){
               masu[0][2].setText(SIRO);
               saveData.add(new SaveMasu(0, 2, SIRO));
               return;
               }
               
           }else if(masu[0][2].getText().equals(collar) && masu[2][0].getText().equals(collar)){
               //　　●
               //　空
               //●
               if(masu[1][1].getText().equals(KARA)){
               masu[1][1].setText(SIRO);
               saveData.add(new SaveMasu(1, 1, SIRO));
               return;
               }
           }
    collar = KURO;
    }
    
    ///初手
        if (masu[0][0].getText().equals(KURO) || masu[0][2].getText().equals(KURO) || masu[2][0].getText().equals(KURO) || masu[2][2].getText().equals(KURO)) {
            //黒が一手目を隅に置いたら真ん中に打つ
            if(masu[1][1].getText().equals(KARA)){
            masu[1][1].setText(SIRO);
               saveData.add(new SaveMasu(1, 1, SIRO));
            return;
            }
        }
   
    //黒が一手目を真ん中に打ったら隅に打つ
         
    if(masu[1][1].getText().equals(KURO)){
        if (masu[0][0].getText().equals(KARA)) {
            //左上
            masu[0][0].setText(SIRO);
               saveData.add(new SaveMasu(0, 0, SIRO));
            return;       
        }else if (masu[0][2].getText().equals(KARA)) {       
            //右上
            masu[0][2].setText(SIRO);
               saveData.add(new SaveMasu(0, 2, SIRO));
            return;       
        }else if (masu[2][0].getText() == KARA) {
            //左下
            masu[2][0].setText(SIRO);
               saveData.add(new SaveMasu(2, 0, SIRO));
            return;    
        }else if (masu[2][2].getText().equals(KARA)) {
            //右下              
            masu[2][2].setText(SIRO);
               saveData.add(new SaveMasu(2, 2, SIRO));
            return;       
        }    
    }
           
    //黒が辺に置いた場合
        if (masu[1][0].getText().equals(KURO) || masu[0][1].getText().equals(KURO) || masu[2][1].getText().equals(KURO) || masu[1][2].getText().equals(KURO)) {
               if(masu[1][1].getText().equals(KARA)){
               masu[1][1].setText(SIRO);
               saveData.add(new SaveMasu(1, 1, SIRO));
               return;
               }
           }
           
           
           //黒白共にどこも揃っていなかったら辺を取りに行く
        if (masu[0][1].getText().equals(KARA)) {
            masu[0][1].setText(SIRO);
               saveData.add(new SaveMasu(0, 1, SIRO));
            return;
               
        }else if(masu[1][0].getText().equals(KARA)){
            masu[1][0].setText(SIRO);
               saveData.add(new SaveMasu(1, 0, SIRO));
            return;
 
        }else if(masu[1][2].getText().equals(KARA)){
            masu[1][2].setText(SIRO);
               saveData.add(new SaveMasu(1, 2, SIRO));
            return;
          
        }else if(masu[2][1].getText().equals(KARA)){
            masu[2][1].setText(SIRO);
               saveData.add(new SaveMasu(2, 1, SIRO));
            return;
        }
        
        
        if (masu[0][0].getText().equals(KARA)) {
            masu[0][0].setText(SIRO);
               saveData.add(new SaveMasu(0, 0, SIRO));
            return;
               
        }else if(masu[0][2].getText().equals(KARA)){
            masu[0][2].setText(SIRO);
               saveData.add(new SaveMasu(0, 2, SIRO));
            return;
 
        }else if(masu[2][0].getText().equals(KARA)){
            masu[2][0].setText(SIRO);
               saveData.add(new SaveMasu(2, 0, SIRO));
            return;
          
        }else if(masu[2][2].getText().equals(KARA)){
            masu[2][2].setText(SIRO);
               saveData.add(new SaveMasu(2, 2, SIRO));
            return;
        }
}
/////////
    void check(){
    System.out.println("判定"); 
    
    int k = 0;
    collar = SIRO;
    
    for (k = 0; k < 2; k++) {
       for (j = 0; j < 3; j++){
       
           //縦に揃ってるかの判定 
           if (masu[0][j].getText().equals(collar) && masu[1][j].getText().equals(collar) && masu[2][j].getText().equals(collar)) {
               win = true;
               return;
           }
       
        for (i = 0;  i< 3; i++) {
           //横に揃っているか判定
           if (masu[i][0].getText().equals(collar) && masu[i][1].getText().equals(collar) && masu[i][2].getText().equals(collar)){
               win = true;
               return;
           }
       }
       }
        
       
       
           //クロスに揃っているか判定
           if (masu[0][0].getText().equals(collar) && masu[1][1].getText().equals(collar) && masu[2][2].getText().equals(collar)){
               win = true;
               return;
           }
           if (masu[0][2].getText().equals(collar) && masu[1][1].getText().equals(collar) && masu[2][0].getText().equals(collar)){
               win = true;
               return;
           }
            collar = KURO;
       }
    }

    void draw(){
    if (!masu[0][0].getText().equals(KARA) && !masu[0][1].getText().equals(KARA) && !masu[0][2].getText().equals(KARA) &&
        !masu[1][0].getText().equals(KARA) &&!masu[1][1].getText().equals(KARA) &&!masu[1][2].getText().equals(KARA) &&
        !masu[2][0].getText().equals(KARA) &&!masu[2][1].getText().equals(KARA) &&!masu[2][2].getText().equals(KARA) ) {
        draw = true;
    }
}

}