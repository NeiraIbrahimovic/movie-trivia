import java.util.ArrayList;
import java.util.Arrays;

import file.MovieDB;
import movies.Actor;
import movies.Movie;

/**
 * Movie trivia class providing different methods for querying and updating a movie database.
 */
public class MovieTrivia {
	
	/**
	 * Create instance of movie database
	 */
	MovieDB movieDB = new MovieDB();
	
	
	public static void main(String[] args) {
		
		//create instance of movie trivia class
		MovieTrivia mt = new MovieTrivia();
		
		//setup movie trivia class
		mt.setUp("moviedata.txt", "movieratings.csv");
	}
	
	/**
	 * Sets up the Movie Trivia class
	 * @param movieData .txt file
	 * @param movieRatings .csv file
	 */
	public void setUp(String movieData, String movieRatings) {
		//load movie database files
		movieDB.setUp(movieData, movieRatings);
		
		//print all actors and movies
		this.printAllActors();
		this.printAllMovies();		
	}
	
	/**
	 * Prints a list of all actors and the movies they acted in.
	 */
	public void printAllActors () {
		System.out.println(movieDB.getActorsInfo());
	}
	
	/**
	 * Prints a list of all movies and their ratings.
	 */
	public void printAllMovies () {
		System.out.println(movieDB.getMoviesInfo());
	}
	
	/**
	 * Inserts given actor and his/her movies into database. If actor already exists, updates the movie list with new movies and avoids duplicates
	 * @param actor name as a string
	 * @param movies String array of movie names that the actor has acted in
	 * @param actorsInfo ArrayList that is to be inserted into/updated
	 */
	public void insertActor (String actor, String[] movies, ArrayList <Actor> actorsInfo) {
		
		// Make the actor's name lowercase and remove whitespace
		String normalizedActorName = normalizeString(actor);
		
		// Make the movie names lowercase and remove whitespace
		for (int i = 0; i < movies.length; i++) {
	        movies[i] = normalizeString(movies[i]);;
		}
		
	    // Find the actor in the actorsInfo list if they already exist
	    Actor existingActor = null; // Set the existingActor to null so this variable remains null unless the actor already exists
	    // Iterate through every actor in the ArrayList
	    for (Actor a : actorsInfo) {
	    	// If the actor's name we are inserting already exists, set the existingActor variable to that name and break the loop
	    	if (a.getName().equals(normalizedActorName)) {
		           existingActor = a; 
		                break;
	    	     }
	      }
	    
	    
	    // If the actor does not already exist, add them to the list with the specified movie list
	    if (existingActor == null) {
	    	// Create a new Actor with the normalized name
	        Actor newActor = new Actor(normalizedActorName);
	    	// Get the ArrayList of movies cast and add the new movies
	    	newActor.getMoviesCast().addAll(Arrays.asList(movies));
	    	// Add the actor to the actorsInfo ArrayList
	    	actorsInfo.add(newActor);	
	    }
	    else {
	    	// If the actor already exists, add the movies without duplicating
	    	// Iterate over every movie in the array of movies
	    	for (String movie : movies) {
	    		// if the movie list for the existingActor does not contain the current movie, add that movie to the list
	            if (!existingActor.getMoviesCast().contains(movie)) {
	                existingActor.getMoviesCast().add(movie);
	            }
	        }
	    }
	}
	
	/**
	 * Inserts given ratings for a given movie into database. If movie already exists, updates the ratings based on given ratings.
	 * @param movie name as a string
	 * @param ratings int array with 2 elements: the critic's rating at index 0 and the audience rating at index 1
	 * @param moviesInfo ArrayList that is to be inserted into/updated
	 */
	public void insertRating (String movie, int [] ratings, ArrayList <Movie> moviesInfo) {
		
		// Check if the ratings in the ratings array are accurate
	    if (ratings == null || ratings.length != 2 || ratings[0] < 0 || ratings[0] > 100 || ratings[1] < 0 || ratings[1] > 100) {
	        return;
	    }
		
		// Make the movie's name lowercase and remove whitespace
		String normalizedMovieName = normalizeString(movie);
		
		// Find the movie in the moviesInfo list if it already exists
	    Movie existingMovie = null; // Set the existingMovie to null so this variable remains null unless the movie already exists
	    // Iterate through every movie in the ArrayList
	    for (Movie m : moviesInfo) {
	    	// If the movie's name we are inserting already exists, set the existingMovie variable to that name and break the loop
	    	if (m.getName().equals(normalizedMovieName)) {
		           existingMovie = m; 
		                break;
	    	     }
	      }
	    
	    // If the movie does not already exist, add it to the moviesInfo ArrayList with the ratings set
	    if (existingMovie == null) {
	    	// Create a new Movie with the normalized name, critic's rating, and audience's rating
	        Movie newMovie = new Movie(normalizedMovieName, ratings[0], ratings[1]);
	    	// Add the new movie to the moviesInfo ArrayList
	    	moviesInfo.add(newMovie);	
	    }
	    //If the movie already exists, update the ratings for the movie
	    else {
	    	//Update the ratings for the existing movie
	    	existingMovie.setCriticRating(ratings[0]);
	    	existingMovie.setAudienceRating(ratings[1]);
	    }
	}
	
	/**
	 * Given an actor, returns the list of all movies. Given a non-existent actor, returns an empty list.
	 * @param actor name of actor as a String
	 * @param actorsInfo ArrayList to get the data from
	 * @return list of all movies for a given actor
	 */
	public ArrayList <String> selectWhereActorIs (String actor, ArrayList <Actor> actorsInfo){
		
		// Make the actor's name lowercase and remove whitespace
		String normalizedActorName = normalizeString(actor);
		
		// Find the actor in the list of actors
		// Iterate through the list of actors
		for (Actor a : actorsInfo) {
			// If the name of the actor in the list matches the name of the given actor, return the list of the actor's movies
			if (a.getName().equals(normalizedActorName)) {
				return a.getMoviesCast();
			}
		}
			
		// If the actor does not exist, return an empty list
			return new ArrayList<String>();
		}			
		
	/**
	 * Given a movie, returns the list of actors in that movie. Given a non-existent movie, returns an empty list.
	 * @param movie name of movie as a String
	 * @param actorsInfo ArrayList to get the data from
	 * @return list of actors' names in the given movie 
	 */
	public ArrayList <String> selectWhereMovieIs (String movie, ArrayList <Actor> actorsInfo){
		
		// Make the movie's name lowercase and remove whitespace
		String normalizedMovieName = normalizeString(movie);;
		
		// Initialize a list of actors
		ArrayList <String> actorsInMovie = new ArrayList <String> ();
		
		// Iterate through every actor in the given list of actors
		for (Actor a : actorsInfo) {
			// Iterate through the movies for every actor
			for (String actorMovie: a.getMoviesCast()) {
			  // If the given movie exists in the actor's list of movies, add the actor's name to the new list of actors
				if (actorMovie.equals(normalizedMovieName)) {
				  actorsInMovie.add(a.getName());
			  }
		  }
		
	   }
		
		// return the list of actors' names. If the movie was non-existent, this will return an empty list
		return actorsInMovie;
   }
	
	/**
	 * Returns a list of movies that satisfy an inequality or equality, based on the comparison argument and the targeted rating argument
	 * @param comparison either '=', '>', or '<' passed as a char. Represents the inequality being tested.
	 * @param targetRating integer representing the targetRating to compare against
	 * @param isCritic boolean representing whether we are interested in the critics rating or the audience rating. true = critic ratings, false = audience ratings.
	 * @param moviesInfo list of movies
	 * @return list of movies that satisfy the given inequality or equality, based on the comparison argument and the targeted rating argument
	 */
	public ArrayList <String> selectWhereRatingIs (char comparison, int targetRating, boolean isCritic, ArrayList <Movie> moviesInfo){
		
		// Check if the comparison is a correct inequality or equality sign ('<', '>', or '=')
		if (comparison != '<' && comparison != '>' && comparison != '=') {
			// Return an empty list
			return new ArrayList<String>(); 
		}
		
		// Check if the targetRating is within the correct range (0 <= targetRating <= 100)
		if (targetRating < 0 || targetRating > 100) {
			// Return an empty list
			return new ArrayList<String>();
		}
		
		// Initialize a list of movies to store the movies that meet the target criteria
		ArrayList <String> targetMovies = new ArrayList <String> ();
		
		// Iterate through the movies in the list
		for (Movie m : moviesInfo) {
			
			// Initialize an int variable to store the rating
			int rating;
			
			// Determine the rating based on whether it's critic or audience rating
			// If isCritic is set to true, store the critic rating for the movie in the rating variable
			if (isCritic) {
				rating = m.getCriticRating();
			}
			// If isCritic is set to false, store the critic rating for the movie in the rating variable
			else {
					rating = m.getAudienceRating();
				}
				
				// Check whether the rating matches the equality/inequality with the target rating
				// If so, add the movie name to the list
		        if ((comparison == '=' && rating == targetRating) ||
		            (comparison == '>' && rating > targetRating) ||
		            (comparison == '<' && rating < targetRating)) {
		            targetMovies.add(m.getName());
		        }
		}
		
		// Return list of movies
		   return targetMovies;
	}
	
	/**
	 * Given an actor and the list of actors, returns a new list of all actors that the given actor has ever worked with in any movie except the actor herself/himself.
	 * @param actor name of actor as a String
	 * @param actorsInfo ArrayList to search through
	 * @return list of all actors that the given actor has ever worked with in any movie except the actor herself/himself
	 */
	public ArrayList <String> getCoActors (String actor, ArrayList <Actor> actorsInfo){
		
		// Initialize a list to store the list of co-actors
		ArrayList <String> listOfCoactors = new ArrayList <String> ();
		
				
		// Convert the given actor string to a normalized actor object and store it in a variable
		Actor actorObject = actorObjectFromActorString (actor, actorsInfo);
		
		 
		// If the given actor does not exist in the list of actors, return an empty list
		if (actorObject == null) {
			return new ArrayList<String>();
		}
		
		// Iterate over the list of actors
		for (Actor a : actorsInfo) {
			// Retrieve the list of all movies for each actor we are iterating through
			// For every movie the current actor we are iterating through has acted in, compare it with the list of movies for the given actor
			for (String movie : selectWhereActorIs(a.getName(), actorsInfo)) {
				// Check if the list of movies for the given actor contains the movie of the actor we are currently iterating through
				if (actorObject.getMoviesCast().contains(movie)){
					// If so, ensure that the actor we are currently iterating through is not the given actor
					if (!a.getName().equals(actorObject.getName())) {
						// If the actor we are currently iterating through is not the same as the given actor, add the actor we are iterating through to the list of coactors
						listOfCoactors.add(a.getName());
					}
					
				}
			}
		}
		
		// Return the list of coactors
		return listOfCoactors;
		
	}
	
	/**
	 * Given two actor names, returns a list of movie names where both actors were cast
	 * @param actor1 actor name as String
	 * @param actor2 actor name as String
	 * @param actorsInfo ArrayList of actors to search through
	 * @return list of movie names where both actors were cast
	 */
	public ArrayList <String> getCommonMovie (String actor1, String actor2, ArrayList <Actor> actorsInfo){
		
		// Initialize a list of movie names both actors were cast in
		ArrayList <String> commonMovies = new ArrayList <String> ();
		
		// Find the actor objects associated with the actor strings
		Actor actor1Object = actorObjectFromActorString (actor1, actorsInfo);
		Actor actor2Object = actorObjectFromActorString (actor2, actorsInfo);
		
		// Iterate through the list of movies actor 1 was cast in
		for (String actor1Movie : actor1Object.getMoviesCast()) {
			// Iterate through the list of actors for the current iteration of movie
			for (String actor : selectWhereMovieIs(actor1Movie, actorsInfo)) {
				// If actor 2 is present in the list of actors, this means actor 1 and actor 2 both acted in this movie
				// Add that movie name to the list
				if (actor.equals(normalizeString(actor2))){
					commonMovies.add(actor1Movie);
				}
			}
		}
		
		// Return list of movie names both actors were in
		return commonMovies;
		
		
	}
	
	/**
	 * Returns a list of movie names that both critics and the audience have rated >= 85
	 * @param moviesInfo ArrayList of movies to search through
	 * @return list of movie names that both critics and the audience have rated >= 85
	 */
	public ArrayList <String> goodMovies (ArrayList <Movie> moviesInfo){
		
		// Initialize a list to store movie names that both critics and the audience have rated >= 85
		ArrayList <String> goodMovies = new ArrayList <String>();
		
		// Create a list of movies where the critic ratings are greater than 85
		ArrayList <String> above85CriticRatings = selectWhereRatingIs ('>', 85, true, moviesInfo);
				
		// Create a list of movies where the critic ratings are equal to 85
		ArrayList <String> equal85CriticRatings = selectWhereRatingIs ('=', 85, true, moviesInfo); 
		
		// Create a list of movies where the critic ratings are greater than or equal to 85 by adding the lists together
		above85CriticRatings.addAll(equal85CriticRatings);
		
		// Create a list of movies where the audience ratings are greater to 85
		ArrayList <String> above85AudienceRatings = selectWhereRatingIs ('>', 85, false, moviesInfo);
		
		// Create a list of movies where the audience ratings are equal to 85
		ArrayList <String> equal85AudienceRatings = selectWhereRatingIs ('=', 85, false, moviesInfo);
		
		// Create a list of movies where the audience ratings are greater than or equal to 85 by adding the lists together
		above85AudienceRatings.addAll(equal85AudienceRatings);
		
		// Iterate through every movie in the list of movies with critic ratings greater than or equal to 85
		for (String movie : above85CriticRatings) {
			// Check if the list of movies with audience ratings greater than or equal to 85 contains this movie
			if (above85AudienceRatings.contains(movie)) {
				// If so, add the movie to the list of good movie names
				goodMovies.add(movie);
			}
		}
		
		// Return the list of good movie names
		return goodMovies;
		
	}
	
	/**
	 * Given a pair of movies, returns a list of actors that acted in both movies. 
	 * @param movie1 name of movie as String
	 * @param movie2 name of movie as String
	 * @return list of actors that acted in both movie1 and movie 2. Empty list if movies have no actors in common.
	 */
	public ArrayList <String> getCommonActors (String movie1, String movie2, ArrayList <Actor> actorsInfo){
		
		// Initialize a list of actors that acted in both movie1 and movie2
		ArrayList <String> commonActors = new ArrayList <String> ();
		
		// Iterate through the list of actors for movie1
		for (String movie1Actor : selectWhereMovieIs (movie1, actorsInfo)){
			// Iterate through the list of movies for the current iteration of actor
			for (String movie : actorObjectFromActorString (movie1Actor, actorsInfo).getMoviesCast()) {
				// If movie 2 is present in the list of movies, this means movie 1 and movie 2 both included the actor
				// Add that actor name to the list
				if (movie.equals(normalizeString(movie2))){
					commonActors.add(movie1Actor);
				}
			}
		}
		
		// Return list of movie names both actors were in
		return commonActors;
	}
	
	/**
	 * Given the moviesInfo DB, this static method returns the mean value of the critics ratings and the audience ratings.
	 * @param moviesInfo ArrayList of movies to search through
	 * @return mean values as a double array, where the 1st item (index 0) is the mean of all critics ratings and the 2nd item (index 1) is the mean of all audience ratings 
	 */
	public static double [] getMean (ArrayList <Movie> moviesInfo) {
		
		// Initialize a double array of length 2
		double [] arrayRatings = new double[2];
		
		// Initialize a variable to hold the sum of the critic ratings
		double criticSum = 0;
		
		// Initialize a variable to hold the sum of the audience ratings
		double audienceSum = 0;
		
		// Initialize a variable to hold the number of movies in the list
		int numberOfMovies = moviesInfo.size();
		
		// Find the average of all the critic ratings
		
		// Iterate over each movie in the list of movies and add the movie's critic rating to the sum
		for (Movie movie : moviesInfo) {
			criticSum += movie.getCriticRating();
			
		}
		
		// Divide the sum of critic ratings by the number of movies
		double criticAverage = (criticSum/numberOfMovies);
		
		// Add the mean of the critic ratings to the double array
		arrayRatings[0] = criticAverage;
		
		// Find the average of all the audience ratings
		
		// Iterate over each movie in the list of movies and add the movie's audience rating to the sum
		for (Movie movie : moviesInfo) {
			audienceSum += movie.getAudienceRating();
		}
		
		// Divide the sum of audience ratings by the number of movies
		double audienceAverage = (audienceSum/numberOfMovies);
		
		// Add the mean of the audience ratings to the double array
		arrayRatings[1] = audienceAverage;
		
		// Return the double array
		return arrayRatings;
		
	}
		
	
    /**
     * Converts the given string to lowercase and removes leading and trailing whitespace. Returns a new, normalized string.
     * @param string representing the actor's name or movie name
     * @return normalized string with lowercase letters and whitespace removed
     */
	public String normalizeString (String input) {
		
		// Make the given string lowercase and remove leading and trailing whitespace
		String normalizedString = input.toLowerCase().trim();
		
		// Return the normalized string
		return normalizedString;
		
	}
	
	/**
	 * Takes the given actor string and returns the associated actor object. If the actor object does not exist, returns null.
	 * @param actor name as string
	 * @param actorsInfo ArrayList holding the list of actors
	 * @return the given actor string as an actor object. Null if the given actor string does not exist in the list of actors.
	 */
	public Actor actorObjectFromActorString (String actor, ArrayList <Actor> actorsInfo){ 
		
		// Make the given string lowercase and remove leading and trailing whitespace
		String normalizedActorName = normalizeString(actor);
		
		// Initialize an Actor variable to store the given actor as an Actor Object
		Actor actorObject = null;
		
		// Find the actor object associated with the actor string
		// Iterate through the list of actors
		for (Actor a : actorsInfo) {
			// Check if the name of the actor matches the given actor's name
			if (a.getName().equals(normalizedActorName)) {
				// If the name of the actor matches the given actor's name, create an actor object storing this actor
				actorObject = a;
				}
		}
		
		// Return actor object
		return actorObject;
		
		}
}


	    
	
	

