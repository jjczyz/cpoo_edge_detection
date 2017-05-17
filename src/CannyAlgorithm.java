import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 * Klasa reprezentuj�ca algorytm Cannyego do detekcji krawi�dzi obrazu
 * @author Ewelina Wardach, Konrad Kara�
 *
 */
public class CannyAlgorithm {

	BufferedImage image;
	
	//Standardowy parametr rozmycia gaussa
	float gaussParameter = 159;
	
	//Operator filtracji Krzy� Robertska
	float[] robertsKernel1 = { 1, 0, 0, -1};
	float[] robertsKernel2 = { 0, 1, -1, 0};
	
	//Operator filtracji Prewitt
	float[] prewittKernel1 = { -1, 0, 1, -1, 0, 1, -1, 0, 1 };
	float[] prewittKernel2 = { 0, 1, 1, -1, 0, 1, -1, -1, 0 };
	float[] prewittKernel3 = { 1, 1, 1, 0, 0, 0, -1, -1, -1 };
	float[] prewittKernel4 = { 1, 1, 0, 1, 0, -1, 0, -1, -1 };
	
	//Operator filtracji Sobel
	float[] sobelKernel1 = { -1, 0, 1, -2, 0, 2, -1, 0, 1 };
	float[] sobelKernel2 = { 0, 1, 2, -1, 0, 1, -2, -1, 0 };
	float[] sobelKernel3 = { 1, 2, 1, 0, 0, 0, -1, -2, -1 };
	float[] sobelKernel4 = { 2, 1, 0, 1, 0, -1, 0, -1, -2 };
	
	//Standardowe parametry progowania histereza
	float thresholdLow = 7;
	float thresholdHigh = 30;
	
	//Zmienne do zarzadzania kolorami pikseli
	Color cellColor[];
	int red[];
	int green[];
	int blue[];
	Color whiteColor;
	int whiteRGB;
	Color blackColor;
	int blackRGB;
	
	//Zmienne do okre�lania stopnia luminacji pikseli
	float lumination[];
	float derivativeLumination[][];
	
	/**
	 * �adowanie pliku z obrazem
	 * @param imageFile plik zawieraj�cy obraz
	 */
	private void loadImage(File imageFile){
		try {
			image = ImageIO.read(imageFile);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, "Problem z wczytaniem pliku!");
			e1.printStackTrace();
		}
	}
	
	/**
	 * Przetwarzanie obrazu: zastosowanie rozmycia gaussa
	 */
	private void applyGaussFilter(){
		float[] gaussKernel = { 2.0f/gaussParameter, 4.0f/gaussParameter, 5.0f/gaussParameter, 4.0f/gaussParameter, 2.0f/gaussParameter,
				4.0f/gaussParameter, 9.0f/gaussParameter, 12.0f/gaussParameter, 9.0f/gaussParameter, 4.0f/gaussParameter,
				5.0f/gaussParameter, 12.0f/gaussParameter, 15.0f/gaussParameter, 12.0f/gaussParameter, 5.0f/gaussParameter,
				4.0f/gaussParameter, 9.0f/gaussParameter, 12.0f/gaussParameter, 9.0f/gaussParameter, 4.0f/gaussParameter,
				2.0f/gaussParameter, 4.0f/gaussParameter, 5.0f/gaussParameter, 4.0f/gaussParameter, 2.0f/gaussParameter
		};
		
		BufferedImageOp op = new ConvolveOp (new Kernel(5,5,gaussKernel));
		image = op.filter(image, null);
	}
	
	/**
	 * Przetwarzanie obrazu: zastosowanie operatora Prewitt (uwydatnienie krawi�dzi)
	 */
	private void applyPrewittFilter(){
		BufferedImageOp op = new ConvolveOp (new Kernel(3,3,prewittKernel1));
		image = op.filter(image, null);
		
		op = new ConvolveOp (new Kernel(3,3,prewittKernel2));
		image = op.filter(image, null);
		
		op = new ConvolveOp (new Kernel(3,3,prewittKernel3));
		image = op.filter(image, null);
		
		op = new ConvolveOp (new Kernel(3,3,prewittKernel4));
		image = op.filter(image, null);
	}
	
	/**
	 * Przetwarzanie obrazu: zastosowanie operatora Sobel (uwydatnienie krawi�dzi)
	 */
	private void applySobelFilter(){
		BufferedImageOp op = new ConvolveOp (new Kernel(3,3,sobelKernel1));
		image = op.filter(image, null);
		
		op = new ConvolveOp (new Kernel(3,3,sobelKernel2));
		image = op.filter(image, null);
		
		op = new ConvolveOp (new Kernel(3,3,sobelKernel3));
		image = op.filter(image, null);
		
		op = new ConvolveOp (new Kernel(3,3,sobelKernel3));
		image = op.filter(image, null);
	}
	
	/**
	 * Przetwarzanie obrazu: zastosowanie operatora Krzy� Robertsa (uwydatnienie krawi�dzi)
	 */
	private void applyRobertsFilter(){
		BufferedImageOp op = new ConvolveOp (new Kernel(2,2,robertsKernel1));
		image = op.filter(image, null);
		
		op = new ConvolveOp (new Kernel(2,2,robertsKernel2));
		image = op.filter(image, null);
	}
	
	/**
	 * Obliczanie pierwszej pochodnej luminacji pikseli
	 */
	private void calculateDerivativeLumination(){
		for(int y=0; y<(image.getHeight()-1); y++){
			derivativeLumination[0][y] = 0;
			
			for(int x=1; x<(image.getWidth()-2); x++){
				cellColor[0] = new Color(image.getRGB(x-1, y));
				cellColor[1] = new Color(image.getRGB(x, y));
				cellColor[2] = new Color(image.getRGB(x+1, y));
				
				
				for(int i=0; i<3; i++){
					red[i] = cellColor[i].getRed();
					green[i] = cellColor[i].getGreen();
					blue[i] = cellColor[i].getBlue();
					lumination[i] = 0.299f*red[i] + 0.587f*green[i] + 0.114f*blue[i];
				}
				
				derivativeLumination[x][y] = (-0.5f)*lumination[0] + (0.5f)*lumination[2];					
			}
		}
	}
	
	/**
	 * Rysowanie kraw�dzi na podstawie progowania histerez�
	 * @param x wsp�rz�dna X piksela odniesienia
	 * @param y wsp�rz�dna Y piksela odniesienia
	 */
	private void drawEdges(int x, int y){
		int cell[][] = new int[9][3];
		if(derivativeLumination[x][y] > thresholdHigh){
			cell[0][0] = (int) derivativeLumination[x-1][y-1];
			cell[0][1] = x-1;
			cell[0][2] = y-1;
			cell[1][0] = (int) derivativeLumination[x][y-1];
			cell[1][1] = x;
			cell[1][2] = y-1;
			cell[2][0] = (int) derivativeLumination[x+1][y-1];
			cell[2][1] = x+1;
			cell[2][2] = y-1;
			cell[3][0] = (int) derivativeLumination[x-1][y];
			cell[3][1] = x-1;
			cell[3][2] = y;
			cell[4][0] = (int) derivativeLumination[x][y];
			cell[4][1] = x;
			cell[4][2] = y;
			cell[5][0] = (int) derivativeLumination[x+1][y];
			cell[5][1] = x+1;
			cell[5][2] = y;
			cell[6][0] = (int) derivativeLumination[x-1][y+1];
			cell[6][1] = x-1;
			cell[6][2] = y+1;
			cell[7][0] = (int) derivativeLumination[x][y+1];
			cell[7][1] = x;
			cell[7][2] = y+1;
			cell[8][0] = (int) derivativeLumination[x+1][y+1];
			cell[8][1] = x+1;
			cell[8][2] = y+1;
			
			for(int i=0; i<9; i++)
				if (cell[i][0] > thresholdLow)
					image.setRGB(cell[i][1], cell[i][2], whiteRGB);
		}
			
		else
			image.setRGB(x, y, blackRGB);
	}
	/**
	 * Rysowanie krawędzi przy pomocy prostego algorytmu
	 */
	private void drawEdgesSimple() {

		BufferedImage imageCopy = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_INT_RGB);
		for(int x=1; x<(image.getWidth()-1); x++)
		{
			for(int y=1; y<(image.getHeight()-1); y++)
			{
				cellColor[0] = new Color(image.getRGB(x-1, y));
				cellColor[1] = new Color(image.getRGB(x+1, y));
				cellColor[2] = new Color(image.getRGB(x, y-1));
				cellColor[3] = new Color(image.getRGB(x, y+1));

				for(int i=0; i<4;i++)
				{
					red[i] = cellColor[i].getRed();
					green[i] = cellColor[i].getGreen();
					blue[i] = cellColor[i].getBlue();
					lumination[i] = (red[i]+green[i]+blue[i])/3;
				}
				if(Math.abs(lumination[0]-lumination[1]) + Math.abs(lumination[2]-lumination[3]) > thresholdLow){
					imageCopy.setRGB(x,y,whiteRGB);
				}
				else
					imageCopy.setRGB(x,y,blackRGB);
			}
		}
		image = imageCopy;
	}
			
	/**
	 * Uruchomienie procesu przetwarzania obrazu
	 * @param imageFile plik z obrazem
	 * @param parameters parametry nastawione przez u�ytkownika
	 */
	public void run(File imageFile, Object[] parameters, boolean useSimple){
		
		this.loadImage(imageFile);
		
		thresholdLow = (int) parameters[0];
		thresholdHigh = (int) parameters[1];
		gaussParameter = (int) parameters[2];
		String filterType = (String) parameters[3];
		
		this.applyGaussFilter();
		
		switch(filterType){
		case "Prewitt": 
			this.applyPrewittFilter();
			break;
		case "Sobel":
			this.applySobelFilter();
			break;
		case "Krzy� Robertsa":
			this.applyRobertsFilter();
			break;
		case "Brak filtru":
			break;
		default:
			break;			
		}
		
		whiteColor = new Color(255,255,255);
		whiteRGB = whiteColor.getRGB();
		blackColor = new Color(0,0,0);
		blackRGB = blackColor.getRGB();
		if(useSimple)
		{
			cellColor = new Color[4];
			red = new int[4];
			green = new int[4];
			blue = new int[4];
			lumination = new float[4];
			drawEdgesSimple();
		}
		else
		{
			cellColor = new Color[3];
			red = new int[3];
			green = new int[3];
			blue = new int[3];
			lumination = new float[3];
			derivativeLumination = new float[image.getWidth()][image.getHeight()];

			this.calculateDerivativeLumination();

			for(int x=1; x<derivativeLumination.length - 1; x++){
				for(int y=1; y<derivativeLumination[x].length - 1; y++){
					this.drawEdges(x,y);
				}
			}
		}
		new ImageFrame(image, filterType);
	}
}
