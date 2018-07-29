/* K-means clustering（K-平均法） */

package 演習実験ⅲ;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class K-means {

	public static void main(String[] args) {
		try {
			String FileName_R = "";
			FileName_R = "/Users/yuka/Desktop/iris.txt";
			int Kj = 20;//num of train data（試験用データの数）
			FileReader filereader = new FileReader(FileName_R);
			BufferedReader br = new BufferedReader(filereader);
			double[][] samples = new double[150][5];
			double train[][] = new double[Kj * 3][5];
			double test[][] = new double[(50 - Kj) * 3][5];
			int train_idx = 0;
			int test_idx = 0;
			double min_dist = 0.0;
			double dist = 0.0;
			int min_j = 0;
			double correct = 0.0;
			double judge = 0.0;
			String str = br.readLine();
			int cluster[] = new int[Kj];//cluster num（クラスタ番号）
			int K = 20;
			double sum[][];
			double ave[][] = new double[K * 3][4];
			int size[];//num of clusters（クラスタの数）
			
			for(int i = 0;i < 150;i++) {
				String[] tmp = (str.split(" ", 0));
				for(int j = 0;j < 5;j++) {
					samples[i][j] = Double.parseDouble(tmp[j]);
				}
				str = br.readLine();
			}
			br.close();
			//separate test data and train data（テストデータと訓練用データに分ける）
			for(int i = 0;i < 150;i++) {
				if(i < Kj || (50 <= i && i < 50 + Kj) || (100 <= i && i < 100 + Kj)) {
					for(int j = 0;j < 5;j++) {
						train[train_idx][j] =  samples[i][j];
					}
					train_idx++;
				}else {
					for(int j = 0;j < 5;j++) {
						test[test_idx][j] =  samples[i][j];
					}
					test_idx++;
				}
			}
			//separate into K clusters(K is num of clusters（K個のクラスタに分ける）
			for(int j = 0;j < K;j++) {
				for(int i = 0;i < Kj / K;i++) {
					cluster[i + Kj / K * j] = j;
					System.out.println(cluster[i + Kj / K * j]);
				}
			}
			sum = new double[K * 3][4];
			//calculate the sum
			for(int c = 0;c < 3;c++) {
				for(int i = 0;i < (Kj / K) * K;i++) {
					for(int j = 0;j < 4;j++) {
						sum[K * c + cluster[i]][j] += train[Kj * c + i][j];
					}
				}
			}
			//calculate the average
			for(int c = 0;c < 3;c++) {
				for(int i = 0;i < K;i++) {
					for(int j = 0;j < 4;j++) {
						ave[c * K + i][j] = sum[c * K + i][j] / (Kj / K);
						System.out.print(ave[c * K + i][j] + " ");
					}
					System.out.println();
				}
			}
			System.out.println();
			for(int c = 0;c < 3;c++) {
				int t = 0;//num of steps（ステップ指標）
				double R = 10000;
				double old_distortion = 10000;
				while(t < 1000 && R >= 0.0001) {
					//strain（歪み）
					double distortion = 0;//strain scale（歪み尺度）
					for(int i = 0;i < Kj;i++) {
						min_dist = 1000;
						min_j = 0;
						for(int j = 0;j < K;j++) {
							dist = 0.0;
							for(int k = 0;k < 4;k++) {
								dist = dist + (train[c * Kj + i][k] - ave[c * K + j][k]) * (train[c * Kj + i][k] - ave[c * K + j][k]);
							}
							if(dist < min_dist) {
								min_dist = dist;
								min_j = j;
							}
						}
						distortion += min_dist;
						//re-clustering（再分割）
						cluster[i] = min_j;
					}
					//initialize array（配列の初期化）
					//update mean vector（平均ベクトルの更新）
					size = new int[K];
					sum = new double[K * 3][4];
					//calculate the sum
					for(int i = 0;i < Kj;i++) {
						for(int j = 0;j < 4;j++) {
							sum[K * c + cluster[i]][j] += train[Kj * c + i][j];
						}
						size[cluster[i]]++;
					}
					//calculate the average
					for(int i = 0;i < K;i++) {
						for(int j = 0;j < 4;j++) {
							if(size[i] != 0) {
								ave[c * K + i][j] = sum[c * K + i][j] / (size[i]);
							}
							System.out.print(ave[c * K + i][j] + " ");
						}
						System.out.println(size[i]);
					}
					System.out.println();
					t++;
					R = (old_distortion - distortion) * (old_distortion - distortion) / (old_distortion * distortion);
					old_distortion = distortion;
				}
				System.out.println();
			}
			for(int i = 0;i < test.length;i++) {
				min_dist = 1000;
				min_j = 0;
				for(int j = 0;j < ave.length;j++) {
					dist = 0.0;
					for(int k = 0;k < 4;k++) {
						dist = dist + (test[i][k] - ave[j][k]) * (test[i][k] - ave[j][k]);
					}
					if(dist < min_dist) {
						min_dist = dist;
						min_j = j;
					}
				}
				if(min_j < K) {
					judge = 1.0;
				}else if(min_j < K * 2) {
					judge = 2.0;
				}else {
					judge = 3.0;
				}		
				if(judge == test[i][4] ) {
					correct++;
				}
			}
			System.out.println("認識精度 : " + correct / test.length * 100);
		}catch(FileNotFoundException e) {
			System.out.println(e);
		}catch(IOException e) {
			System.out.println(e);
		}
	}
}
