import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.SpearmanCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.GenericUserSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class UserRS {

	public static void main(String[] args) throws IOException, TasteException {
		// TODO Auto-generated method stub

		DataModel model = new FileDataModel(new File("movies.csv"));
		
	
		UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
		//UserSimilarity similarity = new LogLikelihoodSimilarity(model);
		//UserSimilarity similarity = new TanimotoCoefficientSimilarity(model);
		
		//UserSimilarity similarity = new EuclideanDistanceSimilarity(model);
		//UserSimilarity similarity = new GenericUserSimilarity(model);
		//UserSimilarity similarity = new SpearmanCorrelationSimilarity(model);

		
		//UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.8, similarity, model);
		UserNeighborhood neighborhood = new NearestNUserNeighborhood(5, similarity, model);
		
		final UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
		
		/*
		 * To display recommendation for all users
		int itemcounter = 1;
		for(LongPrimitiveIterator items = model.getItemIDs();itemcounter<5; items.hasNext())
		{
			long itemId = items.nextLong();
			List<RecommendedItem> myRecommendations = recommender.recommend(itemId, 5);
		for (RecommendedItem myRecommendedItem : myRecommendations) {
			System.out.println("UserID "+itemId+","+"RecommendedMovie  "+myRecommendedItem.getItemID()+","+"RecommendationScore  "+myRecommendedItem.getValue());
		}
		System.out.println("=============================================================================");
		itemcounter++;
		
		}
		*/
		
		List<RecommendedItem> myRecommendations = recommender.recommend(858, 3);
		for (RecommendedItem myRecommendedItem : myRecommendations) {
			System.out.println("UserID: 858 , RecommendedMovie "+myRecommendedItem.getItemID()+","+"RecommendationScore  "+myRecommendedItem.getValue());
		}
		
		
        RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
            public Recommender buildRecommender(DataModel model) throws TasteException {                
               
            	UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
            	//UserSimilarity similarity = new LogLikelihoodSimilarity(model);
            	//UserSimilarity similarity = new TanimotoCoefficientSimilarity(model);
            	//UserSimilarity similarity = new EuclideanDistanceSimilarity(model);  
            	
                        

            	//UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
            	UserNeighborhood neighborhood = new NearestNUserNeighborhood(3, similarity, model);
                return new GenericUserBasedRecommender(model, neighborhood,similarity);                
            }
        };
        

        RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();        
        double score = evaluator.evaluate(recommenderBuilder, null, model, 0.7, 1.0);    
        System.out.println("RMSE: " + score);
            
            RecommenderIRStatsEvaluator statsEvaluator = new GenericRecommenderIRStatsEvaluator();        
            IRStatistics stats = statsEvaluator.evaluate(recommenderBuilder, null, model, null, 10, 1, 1); // evaluate precision recall at 10
            
        System.out.println("Precision: " + stats.getPrecision());
        System.out.println("Recall: " + stats.getRecall());
        System.out.println("F1 Score: " + stats.getF1Measure()); 
		
		  
	}

}
