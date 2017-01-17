package com.example.spgen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RectShape;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.graphics.Color;

public class BoardView extends View {
	private ShapeDrawable mDrawable;
	private ShapeDrawable mDrawable2;
	private ShapeDrawable xlines[];
	private ShapeDrawable ylines[];
	private ArrayList<ShapeDrawable> obstacles;
	private ArrayList<Path> paths;
	private ArrayList<GraphicText> text;
	private ArrayList<GraphicText> text2;
	private int dimension;
	private int widthPixels;
	private int cellWidth;
	Paint paint = new Paint();
	private ShapeDrawable start;
	private ShapeDrawable end;
	private Piece piece;
	GraphicText gText;
	ArrayList<Stack<String>> bigSeqs;
	int sp;
	
	public BoardView(Context context, int dimension, List<List<Integer>> obstacles2, String pieceStr, int start[], int stop[]) {
		super(context);
		this.dimension = dimension;
		xlines = new ShapeDrawable[dimension + 1]; 	
		ylines = new ShapeDrawable[dimension + 1]; ;
		DisplayMetrics metrics = new DisplayMetrics();
		text = new ArrayList<GraphicText>(); 
		((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
		
		int end[] = stop;
	    //int start[] = {2,4}; 
		
		paths = new ArrayList<Path>();
		
		cellWidth = metrics.widthPixels / dimension;
		widthPixels = metrics.widthPixels;
		int currentWidth = 0;
		
		int x = 10;
		int y = 10;
		int width = 300;
		int height = 50;	
		
		//SD_TODO: 64x64 does not work well, probably because of integer devision vs float
		for(int counter = 0; counter <= dimension; counter++){
			xlines[counter] = new ShapeDrawable(new RectShape()); 
			xlines[counter].getPaint().setColor(Color.BLACK);
			xlines[counter].setBounds(0, currentWidth, metrics.widthPixels, currentWidth + metrics.widthPixels/200);
		    ylines[counter] = new ShapeDrawable(new RectShape());
			ylines[counter].getPaint().setColor(Color.BLACK);
			ylines[counter].setBounds(currentWidth, 0, currentWidth + metrics.widthPixels/200, metrics.widthPixels);
			currentWidth+=cellWidth;
		}
		
		createObstacles(obstacles2);
		
	    createStartEnd(start, end);
		spPath path = new spPath(getPiece(pieceStr), dimension, start, end, obstacles2, cellWidth);
		
		path.genBoard();
		
		text = path.getBoard(1);
		text2 = path.getBoard(2); 
		path.generatePaths();
		paths = path.paths;
		bigSeqs = path.bigSeqs;
		sp = path.sp;
		
		/*int tempArr[] = {0,0};
		int tempArr2[] = {1,1}; 
		createPath(tempArr, tempArr2);

		int tempArr3[] = {1,1};
		int tempArr4[] = {1,2}; 
		createPath(tempArr3, tempArr4);
		
		int tempArr5[] = {0,0};
		int tempArr6[] = {1,2};
		
		createStartEnd(tempArr5, tempArr6);
		
		//y,x
		text.add(new GraphicText("0",1,0));
		text.add(new GraphicText("5",2,1));
		*/
	}
	
	protected void onDraw(Canvas canvas) {
		for(int counter = 0; counter <= dimension; counter++){
			xlines[counter].draw(canvas);
			ylines[counter].draw(canvas);
		}	
		Iterator<ShapeDrawable> iterator = this.obstacles.iterator();
		while(iterator.hasNext()){
			iterator.next().draw(canvas);
		}
 
		Paint pathPaint = new Paint();
		pathPaint.setStrokeWidth(3); 
		pathPaint.setPathEffect(null);
		pathPaint.setColor(Color.BLACK);
		pathPaint.setStyle(Paint.Style.STROKE);
		
		//canvas.drawPaint(paint);
		
        Iterator<Path> pIterator = this.paths.iterator();
		while(pIterator.hasNext()){
			canvas.drawPath(pIterator.next(), pathPaint);	
		}
		
		end.draw(canvas);
		start.draw(canvas);
		
		drawTextCustom(canvas);
		drawBigPaths(canvas);

        //canvas.drawPath(path, pathPaint);
		 
		//mDrawable2.draw(canvas);
        //canvas.drawLine(0, 0, 20, 20, paint);
        //canvas.drawLine(20, 0, 0, 20, paint);
	}
	
	public Piece getPiece(String pieceStr){
		Piece newPiece = null;
		if (pieceStr.equals("King") ){
			ArrayList<int[]> moves = new ArrayList<int[]>();
			//y,x
			int up[] = {1,0};
			moves.add(up);
			int down[] = {-1,0};
			moves.add(down);			
			int left[] = {0,-1};
			moves.add(left);
			int right[] = {0,1};
			moves.add(right);		
			int upRight[] = {1,1};
			moves.add(upRight);
			int downRight[] = {-1,1};
			moves.add(downRight);			
			int upLeft[] = {1,-1};
			moves.add(upLeft);
			int downLeft[] = {-1,-1};
			moves.add(downLeft);	
			newPiece = new Piece(pieceStr, moves, false);
		}else if (pieceStr.equals("Knight") ){
			ArrayList<int[]> moves = new ArrayList<int[]>();
			//y,x
			int upLeft[] = {2,-1};
			moves.add(upLeft);
			int downLeft[] = {-2,-1};
			moves.add(downLeft);			
			int upRight[] = {2,1};
			moves.add(upRight);
			int downRight[] = {-2,1};
			moves.add(downRight);		
			newPiece = new Piece(pieceStr, moves, false);
		}else if (pieceStr.equals("Pawn") ){
			//SD_TODO: paths can move back in forth to connect two points
			ArrayList<int[]> moves = new ArrayList<int[]>();
			//y,x
			int up[] = {1,0};
			moves.add(up);
			int down[] = {-1,0};
			moves.add(down);
			newPiece = new Piece(pieceStr, moves, false);
		}else if (pieceStr.equals("Rook") ){
			ArrayList<int[]> moves = new ArrayList<int[]>();
			//y,x
			int up[] = {1,0};
			moves.add(up);
			int down[] = {-1,0};
			moves.add(down);			
			int left[] = {0,-1};
			moves.add(left);
			int right[] = {0,1};
			moves.add(right);
			newPiece = new Piece(pieceStr, moves, true);
		}else if (pieceStr.equals("Bishop") ){
			ArrayList<int[]> moves = new ArrayList<int[]>();
			//y,x
			int upRight[] = {-1,1};
			moves.add(upRight);
			int upLeft[] = {-1,-1};
			moves.add(upLeft);			
			int downRight[] = {1,1};
			moves.add(downRight);
			int downLeft[] = {1,-1};
			moves.add(downLeft);
			newPiece = new Piece(pieceStr, moves, true);
		}else if (pieceStr.equals("Queen") ){
			ArrayList<int[]> moves = new ArrayList<int[]>();
			//y,x
			int upRight[] = {-1,1};
			moves.add(upRight);
			int upLeft[] = {-1,-1};
			moves.add(upLeft);			
			int downRight[] = {1,1};
			moves.add(downRight);
			int downLeft[] = {1,-1};
			moves.add(downLeft);
			int up[] = {1,0};
			moves.add(up);
			int down[] = {-1,0};
			moves.add(down);			
			int left[] = {0,-1};
			moves.add(left);
			int right[] = {0,1};
			moves.add(right);
			newPiece = new Piece(pieceStr, moves, true);
		}else if (pieceStr.equals("Queen") ){
			ArrayList<int[]> moves = new ArrayList<int[]>();
			//y,x
			int upRight[] = {-1,1};
			moves.add(upRight);
			int upLeft[] = {-1,-1};
			moves.add(upLeft);			
			int downRight[] = {1,1};
			moves.add(downRight);
			int downLeft[] = {1,-1};
			moves.add(downLeft);
			int up[] = {1,0};
			moves.add(up);
			int down[] = {-1,0};
			moves.add(down);			
			int left[] = {0,-1};
			moves.add(left);
			int right[] = {0,1};
			moves.add(right);
			newPiece = new Piece(pieceStr, moves, true);
		}else if (pieceStr.equals("Messiah") ){
			ArrayList<int[]> moves = new ArrayList<int[]>();
			//y,x
			int upRight[] = {-2,2};
			moves.add(upRight);
			int upLeft[] = {-2,-2};
			moves.add(upLeft);			
			int downRight[] = {2,2};
			moves.add(downRight);
			int downLeft[] = {2,-2};
			moves.add(downLeft);
			int upRight1[] = {-1,1};
			moves.add(upRight1);
			int upLeft1[] = {-1,-1};
			moves.add(upLeft1);			
			int downRight1[] = {1,1};
			moves.add(downRight1);
			int downLeft1[] = {1,-1};
			moves.add(downLeft1);
			newPiece = new Piece(pieceStr, moves, true);
		}				

		return newPiece;
	}
	
	public void drawTextCustom(Canvas canvas){
		Iterator<GraphicText> iterator = this.text.iterator();
		Paint pathPaint = new Paint();
		pathPaint.setColor(Color.BLUE);
		pathPaint.setTextSize(this.cellWidth/2);
		GraphicText gText;
		while(iterator.hasNext()){
			 gText = iterator.next();	
			 canvas.drawText(gText.text, gText.x * this.cellWidth + this.cellWidth/4, 
					 gText.y * this.cellWidth + 3*this.cellWidth/4, pathPaint);
		}
		Iterator<GraphicText> iterator2 = this.text2.iterator();
		Paint pathPaint2 = new Paint();
		pathPaint2.setColor(Color.GREEN);
		pathPaint2.setTextSize(this.cellWidth/2);
		GraphicText gText2;
		while(iterator2.hasNext()){
			 gText2 = iterator2.next();	
			 canvas.drawText(gText2.text, gText2.x * this.cellWidth + this.cellWidth/2, 
					 gText2.y * this.cellWidth + 3*this.cellWidth/4, pathPaint2);
		}		
	}
	
	public void drawBigPaths(Canvas canvas){
		Paint pathPaint = new Paint();
		pathPaint.setColor(Color.BLUE);
		pathPaint.setTextSize(this.cellWidth/2);
		GraphicText gText;
		Iterator iterator = bigSeqs.iterator();
		int counter = 1;
		while(iterator.hasNext()){
			Stack<String> stack = (Stack<String>) iterator.next();	
			if (stack.size() == sp){
				if(counter <= 3){
					String onePath = new String();
					while (!stack.empty()) {
						onePath += stack.pop();
						onePath += ">";
					}
					GraphicText temp1 = new GraphicText(onePath, this.cellWidth * this.dimension + this.cellWidth * counter, 0);
					canvas.drawText(temp1.text, temp1.x, temp1.y, pathPaint);
				}	
			 	counter++;
			}	
		}
		counter--;
		GraphicText temp1 = new GraphicText("Total paths: " + counter, this.cellWidth * this.dimension + this.cellWidth * 4, 0);
		canvas.drawText(temp1.text, temp1.x, temp1.y, pathPaint);
	}
	
	private void createObstacles(List<List<Integer>> obstacles){
		//create a black square
		//y and x a are zero based
		this.obstacles = new ArrayList<ShapeDrawable>();
		Iterator<List<Integer>> iterator = obstacles.iterator(); 
		while(iterator.hasNext()){
			List<Integer> pair = iterator.next();
			int y = pair.get(0);
			int x = pair.get(1);
			ShapeDrawable obstacle = new ShapeDrawable(new RectShape());
			obstacle.getPaint().setColor(Color.BLACK);
			obstacle.setBounds(x * this.cellWidth + this.widthPixels/200, y * this.cellWidth + this.widthPixels/200, 
					(x + 1) * this.cellWidth, (y + 1) * this.cellWidth);
			this.obstacles.add(obstacle);
		}	
	}
	
	private void createPath(int start[], int end[]){
		Path path = new Path();
		//x,y
		path.moveTo(start[1] * this.cellWidth + this.cellWidth / 2, start[0] * this.cellWidth + this.cellWidth / 2);
		path.lineTo(end[1] * this.cellWidth + this.cellWidth / 2, end[0] * this.cellWidth + this.cellWidth / 2);
		path.close();
		paths.add(path);
	}

	private void createStartEnd(int start[], int end[]){
		this.start = new ShapeDrawable(new OvalShape()); 
		this.end = new ShapeDrawable(new OvalShape()); 
		this.start.getPaint().setColor(Color.BLACK);
		this.end.getPaint().setColor(Color.RED);
		this.start.setBounds(start[1] * this.cellWidth, start[0] * this.cellWidth,
				(start[1] + 1) * this.cellWidth, (start[0] + 1) * this.cellWidth);
		this.end.setBounds(end[1] * this.cellWidth, end[0] * this.cellWidth,
				(end[1] + 1) * this.cellWidth, (end[0] + 1) * this.cellWidth);
	}
		
	public void move(){
		Rect coordinates = mDrawable.getBounds();
		coordinates.offset(50, 50);
		mDrawable.setBounds(coordinates);
	}
}
