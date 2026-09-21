import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import file.MovieDB;
import movies.Actor;

class MovieTriviaTest {

	// instance of movie trivia object to test
	MovieTrivia mt;
	// instance of movieDB object
	MovieDB movieDB;

	@BeforeEach
	void setUp() throws Exception {
		// initialize movie trivia object
		mt = new MovieTrivia();

		// set up movie trivia object
		mt.setUp("moviedata.txt", "movieratings.csv");

		// get instance of movieDB object from movie trivia object
		movieDB = mt.movieDB;
	}

	@Test
	void testSetUp() {
		assertEquals(7, movieDB.getActorsInfo().size(),
				"actorsInfo should contain 7 actors after reading moviedata.txt.");
		assertEquals(7, movieDB.getMoviesInfo().size(),
				"moviesInfo should contain 7 movies after reading movieratings.csv.");

		assertEquals("meryl streep", movieDB.getActorsInfo().get(0).getName(),
				"\"meryl streep\" should be the name of the first actor in actorsInfo.");
		assertEquals(3, movieDB.getActorsInfo().get(0).getMoviesCast().size(),
				"The first actor listed in actorsInfo should have 3 movies in their moviesCasted list.");
		assertEquals("doubt", movieDB.getActorsInfo().get(0).getMoviesCast().get(0),
				"\"doubt\" should be the name of the first movie in the moviesCasted list of the first actor listed in actorsInfo.");

		assertEquals("doubt", movieDB.getMoviesInfo().get(0).getName(),
				"\"doubt\" should be the name of the first movie in moviesInfo.");
		assertEquals(79, movieDB.getMoviesInfo().get(0).getCriticRating(),
				"The critics rating for the first movie in moviesInfo is incorrect.");
		assertEquals(78, movieDB.getMoviesInfo().get(0).getAudienceRating(),
				"The audience rating for the first movie in moviesInfo is incorrect.");
	}

	@Test
	void testInsertActor() {

		// try to insert new actor with new movies
		mt.insertActor("test1", new String[] { "testmovie1", "testmovie2" }, movieDB.getActorsInfo());
		assertEquals(8, movieDB.getActorsInfo().size(),
				"After inserting an actor, the size of actorsInfo should have increased by 1.");
		assertEquals("test1", movieDB.getActorsInfo().get(movieDB.getActorsInfo().size() - 1).getName(),
				"After inserting actor \"test1\", the name of the last actor in actorsInfo should be \"test1\".");
		assertEquals(2, movieDB.getActorsInfo().get(movieDB.getActorsInfo().size() - 1).getMoviesCast().size(),
				"Actor \"test1\" should have 2 movies in their moviesCasted list.");
		assertEquals("testmovie1",
				movieDB.getActorsInfo().get(movieDB.getActorsInfo().size() - 1).getMoviesCast().get(0),
				"\"testmovie1\" should be the first movie in test1's moviesCasted list.");

		// try to insert existing actor with new movies
		mt.insertActor("   Meryl STReep      ", new String[] { "   DOUBT      ", "     Something New     " },
				movieDB.getActorsInfo());
		assertEquals(8, movieDB.getActorsInfo().size(),
				"Since \"meryl streep\" is already in actorsInfo, inserting \"   Meryl STReep      \" again should not increase the size of actorsInfo.");
		assertEquals(4, movieDB.getActorsInfo().get(0).getMoviesCast().size(),
				"Actor \"meryl streep\" should have 4 movies in their moviesCasted list.");
		assertEquals("something new",
				movieDB.getActorsInfo().get(0).getMoviesCast().get(movieDB.getActorsInfo().get(0).getMoviesCast().size() - 1),
				"\"something new\" should be the last movie in meryl streep's moviesCasted list.");

		// look up and inspect movies for existing actor
		// note, this requires the use of properly implemented selectWhereActorIs method
		// you can comment out these two lines until you have a selectWhereActorIs
		// method
		assertEquals(4, mt.selectWhereActorIs("meryl streep", movieDB.getActorsInfo()).size(),
				"After inserting Meryl Streep again with 2 movies, only one of which is not on the list yet, the number of movies \"meryl streep\" appeared in should be 4.");
		assertTrue(mt.selectWhereActorIs("meryl streep", movieDB.getActorsInfo()).contains("something new"),
				"After inserting Meryl Streep again with a new Movie \"     Something New     \", \"somenthing new\" should appear as one of the movies she has appeared in.");
		
		// try to insert existing actor with existing movies
		mt.insertActor("   meryl STREEP   ", new String[] {"doubt", "something new    ", "the POST"}, movieDB.getActorsInfo());
		assertEquals(8, movieDB.getActorsInfo().size(),
				"Since \"meryl streep\" is already in actorsInfo, inserting \"   Meryl STReep      \" again should not increase the size of actorsInfo.");
		assertEquals(4, movieDB.getActorsInfo().get(0).getMoviesCast().size(),
				"Since the movies are already in the moviesCasted list, Actor \"meryl streep\" should still have 4 movies in their moviesCasted list.");	

	}

	@Test
	void testInsertRating() {

		// try to insert new ratings for new movie
		mt.insertRating("testmovie", new int[] { 79, 80 }, movieDB.getMoviesInfo());
		assertEquals(8, movieDB.getMoviesInfo().size(),
				"After inserting ratings for a movie that is not in moviesInfo yet, the size of moviesInfo should increase by 1.");
		assertEquals("testmovie", movieDB.getMoviesInfo().get(movieDB.getMoviesInfo().size() - 1).getName(),
				"After inserting a rating for \"testmovie\", the name of the last movie in moviessInfo should be \"testmovie\".");
		assertEquals(79, movieDB.getMoviesInfo().get(movieDB.getMoviesInfo().size() - 1).getCriticRating(),
				"The critics rating for \"testmovie\" is incorrect.");
		assertEquals(80, movieDB.getMoviesInfo().get(movieDB.getMoviesInfo().size() - 1).getAudienceRating(),
				"The audience rating for \"testmovie\" is incorrect.");

		// try to insert new ratings for existing movie
		mt.insertRating("doubt", new int[] { 100, 100 }, movieDB.getMoviesInfo());
		assertEquals(8, movieDB.getMoviesInfo().size(),
				"Since \"doubt\" is already in moviesInfo, inserting ratings for it should not increase the size of moviesInfo.");

		// look up and inspect movies based on newly inserted ratings
		// note, this requires the use of properly implemented selectWhereRatingIs
		// method
		// you can comment out these two lines until you have a selectWhereRatingIs
		// method
		assertEquals(1, mt.selectWhereRatingIs('>', 99, true, movieDB.getMoviesInfo()).size(),
				"After inserting a critic rating of 100 for \"doubt\", there should be 1 movie in moviesInfo with a critic rating greater than 99.");
		assertTrue(mt.selectWhereRatingIs('>', 99, true, movieDB.getMoviesInfo()).contains("doubt"),
				"After inserting the rating for \"doubt\", \"doubt\" should appear as a movie with critic rating greater than 99.");
		
		// try to insert existing ratings for existing movies (should keep the ratings the same)
		mt.insertRating("doubt", new int[] { 79, 78 }, movieDB.getMoviesInfo());
		assertEquals(8, movieDB.getMoviesInfo().size(),
				"Since \"doubt\" is already in moviesInfo, inserting ratings for it should not increase the size of moviesInfo.");
		assertEquals(79, movieDB.getMoviesInfo().get(0).getCriticRating(),
				"The critics rating for \"doubt\" should stay the same.");
		assertEquals(78, movieDB.getMoviesInfo().get(0).getAudienceRating(),
				"The audience rating for \"doubt\" should stay the same.");
		
		
		// try to insert null ratings for new movies
		mt.insertRating("testmovie", null, movieDB.getMoviesInfo());
		assertEquals(8, movieDB.getMoviesInfo().size(),
				"After inserting null ratings for a movie that is not in moviesInfo yet, the size of moviesInfo should still be 8.");
		
		// try to insert a list of ratings with length greater than 2 
		mt.insertRating("testmovie", new int[] {78, 89, 93}, movieDB.getMoviesInfo());
		assertEquals(8, movieDB.getMoviesInfo().size(),
				"After inserting a ratings list with length greater than 2 for a movie that is not in moviesInfo yet, the size of moviesInfo should still be 8.");
		
		// try to insert a list of ratings with length less than 2
		mt.insertRating("testmovie", new int[] {78}, movieDB.getMoviesInfo());
		assertEquals(8, movieDB.getMoviesInfo().size(),
				"After inserting a ratings list with length of 1 for a movie that is not in moviesInfo yet, the size of moviesInfo should still be 8.");
		
		// try to insert an empty list of ratings 
		mt.insertRating("testmovie", new int[] {}, movieDB.getMoviesInfo());
		assertEquals(8, movieDB.getMoviesInfo().size(),
				"After inserting an empty list of ratings for a movie that is not in moviesInfo yet, the size of moviesInfo should still be 8.");
		
	
		// try to insert negative rating for new movie
		mt.insertRating("testmovie", new int[] {-1, 78}, movieDB.getMoviesInfo());
		assertEquals(8, movieDB.getMoviesInfo().size(),
				"After inserting negative ratings for a movie that is not in moviesInfo yet, the size of moviesInfo should still be 8.");
		
		// try to insert rating > 100 for new movie
		mt.insertRating("testmovie", new int[] {76, 120}, movieDB.getMoviesInfo());
		assertEquals(8, movieDB.getMoviesInfo().size(),
				"After inserting ratings > 100 for a movie that is not in moviesInfo yet, the size of moviesInfo should still be 8.");
	}

	@Test
	void testSelectWhereActorIs() {
		
		// try to retrieve list of movies for an existing actor
		assertEquals(3, mt.selectWhereActorIs("meryl streep", movieDB.getActorsInfo()).size(),
				"The number of movies \"meryl streep\" has appeared in should be 3.");
		assertEquals("doubt", mt.selectWhereActorIs("meryl streep", movieDB.getActorsInfo()).get(0),
				"\"doubt\" should show up as first in the list of movies \"meryl streep\" has appeared in.");
		assertTrue(mt.selectWhereActorIs("meryl streep", movieDB.getActorsInfo()).contains("the post"),
				"\"the post\" should appear as one of the movies mery streep has acted in.");
		

		// try to retrieve the list of movies for a non-existing actor
		assertEquals(0, mt.selectWhereActorIs("nonexisting actor", movieDB.getActorsInfo()).size(),
				"The number of movies \"nonexisting actor\" has appeared in should be 0.");
		assertFalse(mt.selectWhereActorIs("nonexisting actor", movieDB.getActorsInfo()).contains("movie"),
				"There should be an empty list returned for a nonexisting actor");
		
		// try to retrieve the list for an existing actor with different casing
		assertEquals(3, mt.selectWhereActorIs("MeryL STREEp", movieDB.getActorsInfo()).size(),
				"The number of movies \"MeryL STREEp\" has appeared in should be 3 because the names should not be case sensitive.");
		assertEquals("doubt", mt.selectWhereActorIs("MeryL STREEp", movieDB.getActorsInfo()).get(0),
				"\"doubt\" should show up as first in the list of movies \"MeryL STREEp\" has appeared in because the names should not be case sensitive.");
		
		// try to retrieve the list for an existing actor with leading or trailing whitespace
		assertEquals(3, mt.selectWhereActorIs("     tom hanks    ", movieDB.getActorsInfo()).size(),
				"The number of movies \"     tom hanks    \" has appeared in should be 3 because the names should not include leading or trailing whitespace.");
		assertEquals("the post", mt.selectWhereActorIs("     tom hanks    ", movieDB.getActorsInfo()).get(0),
				"\"the post\" should show up as first in the list of movies \"     tom hanks    \" has appeared in because the names should not include leading or trailing whitespace.");
		
		
	}

	@Test
	void testSelectWhereMovieIs() {
		
		// try to retrieve the list of actors in a given movie
		assertEquals(2, mt.selectWhereMovieIs("doubt", movieDB.getActorsInfo()).size(),
				"There should be 2 actors in \"doubt\".");
		assertEquals(true, mt.selectWhereMovieIs("doubt", movieDB.getActorsInfo()).contains("meryl streep"),
				"\"meryl streep\" should be an actor who appeared in \"doubt\".");
		assertEquals(true, mt.selectWhereMovieIs("doubt", movieDB.getActorsInfo()).contains("amy adams"),
				"\"amy adams\" should be an actor who appeared in \"doubt\".");

		// try to retrieve the list of actors for a non-existent movie (should return an empty list)
		assertEquals(0, mt.selectWhereMovieIs("nonexistent movie", movieDB.getActorsInfo()).size(),
				"There should be 0 actors in \"nonexistent movie\". Calling the SelectWhereMovieIs() method should return an empty list.");
		assertEquals(false, mt.selectWhereMovieIs("nonexistent movie", movieDB.getActorsInfo()).contains("meryl streep"),
				"\"meryl streep\" should not be an actor who appeared in \"nonexistent movie\" as this movie does not exist.");
		
		// try to retrieve the list of actors for a given movie with different casing
		assertEquals(2, mt.selectWhereMovieIs("DOUbt", movieDB.getActorsInfo()).size(),
				"There should be 2 actors in \"DOUbt\" because movies should be case insensitive.");
		assertEquals(true, mt.selectWhereMovieIs("DOUbt", movieDB.getActorsInfo()).contains("meryl streep"),
				"\"meryl streep\" should be an actor who appeared in \"DOUbt\" because movies should be case insensitive.");
		
		// try to retrieve the list of actors for given movie with leading an trailing whitespace
		assertEquals(2, mt.selectWhereMovieIs("    doubt    ", movieDB.getActorsInfo()).size(),
				"There should be 2 actors in \"    doubt    \" because movies should not include leading or trailing whitespace.");
		assertEquals(true, mt.selectWhereMovieIs("    doubt    ", movieDB.getActorsInfo()).contains("meryl streep"),
				"\"meryl streep\" should be an actor who appeared in \"    doubt    \" because movies should not include leading or trailing whitespace.");

		
	}

	@Test
	void testSelectWhereRatingIs() {
		// try to retrieve the list of movies with different inequalities for ratings
		assertEquals(6, mt.selectWhereRatingIs('>', 0, true, movieDB.getMoviesInfo()).size(),
				"There should be 6 movies where critics rating is greater than 0.");
		assertEquals(0, mt.selectWhereRatingIs('=', 65, false, movieDB.getMoviesInfo()).size(),
				"There should be no movie where audience rating is equal to 65.");
		assertEquals(2, mt.selectWhereRatingIs('<', 30, true, movieDB.getMoviesInfo()).size(),
				"There should be 2 movies where critics rating is less than 30.");

		// try to retrieve the list of movies with targetRating out of range (0 <= targetRating <= 100) (should return an empty list)
		assertEquals(0, mt.selectWhereRatingIs('>', -10, true, movieDB.getMoviesInfo()).size(),
				"There should be an empty list returned where critics rating is greater than -10, because -10 is out of range for the targetRating and this should return an empty list.");
		assertEquals(0, mt.selectWhereRatingIs('<', 120, true, movieDB.getMoviesInfo()).size(),
				"There should be an empty list returned where critics rating is less than 120, because 120 is out of range for the targetRating and this should return an empty list.");
		
		// try to retrieve the list of movies with incorrect comparison (should return an empty list)
		assertEquals(0, mt.selectWhereRatingIs('?', 98, true, movieDB.getMoviesInfo()).size(),
				"There should be an empty list returned when an unknown inequality is given as input.");
		assertEquals(0, mt.selectWhereRatingIs('x', 120, true, movieDB.getMoviesInfo()).size(),
				"There should be an empty list returned when an unknown inequality is given as input.");
		
		// try to retrieve the list of movies for an inequality that none of the movies meet (should return an empty list)
		assertEquals(0, mt.selectWhereRatingIs('=', 33, true, movieDB.getMoviesInfo()).size(),
				"There should be an empty list returned when none of the movies meet the inequality.");
		assertEquals(0, mt.selectWhereRatingIs('>', 95, false, movieDB.getMoviesInfo()).size(),
				"There should be an empty list returned when none of the movies meet the inequality.");
	
	}

	@Test
	void testGetCoActors() {
		
		// try to retrieve a list of co-actors for an existing actor with co-actors
		assertEquals(3, mt.getCoActors("meryl streep", movieDB.getActorsInfo()).size(),
				"\"meryl streep\" should have 3 co-actors.");
		assertTrue(mt.getCoActors("meryl streep", movieDB.getActorsInfo()).contains("tom hanks"),
				"\"tom hanks\" was a co-actor of \"meryl streep\".");
		assertTrue(mt.getCoActors("meryl streep", movieDB.getActorsInfo()).contains("amy adams"),
				"\"amy adams\" was a co-actor of \"meryl streep\".");
		assertTrue(mt.getCoActors("meryl streep", movieDB.getActorsInfo()).contains("pretend actor"),
				"\"pretend actor\" was a co-actor of \"meryl streep\".");
		
		// try to retrieve a list of co-actors for an existing actor with no co-actors
		assertEquals(0, mt.getCoActors("robin williams", movieDB.getActorsInfo()).size(),
				"\"robin williams\" should have 0 co-actors.");
		assertFalse(mt.getCoActors("robin williams", movieDB.getActorsInfo()).contains("tom hanks"),
				"\"tom hanks\" was not a co-actor of \"robin williams\".");

		// try to retrieve a list of co-actors for a non-existing actor (should return empty list)
		assertEquals(0, mt.getCoActors("nonexistent actor", movieDB.getActorsInfo()).size(),
				"\"nonexistent actor\" should have 0 co-actors because an empty list should be returned.");
		
		// try to retrieve a list of co-actors for an existing actor with different casing
		assertEquals(1, mt.getCoActors("AMY aDaMs", movieDB.getActorsInfo()).size(),
				"\"AMY aDaMs\" should have 1 co-actor, because the name should be case-insensitive.");
		assertTrue(mt.getCoActors("AMY aDaMs", movieDB.getActorsInfo()).contains("meryl streep"),
				"\"meryl streep\" was a co-actor of \"AMY aDaMs\".");
		
		// try to retrieve a list of co-actors for an existing actor with leading and trailing whitespace
		assertEquals(3, mt.getCoActors("    meryl streep       ", movieDB.getActorsInfo()).size(),
				"\"meryl streep\" should have 3 co-actors, because the name should ignore leading and trailing whitespace.");
		assertTrue(mt.getCoActors("    meryl streep       ", movieDB.getActorsInfo()).contains("tom hanks"),
				"\"tom hanks\" was a co-actor of \"    meryl streep       \".");
		assertTrue(mt.getCoActors("    meryl streep       ", movieDB.getActorsInfo()).contains("amy adams"),
				"\"amy adams\" was a co-actor of \"    meryl streep       \".");
		assertTrue(mt.getCoActors("    meryl streep       ", movieDB.getActorsInfo()).contains("pretend actor"),
				"\"pretend actor\" was a co-actor of \"    meryl streep       \".");
	}

	@Test
	void testGetCommonMovie() {
		
		// try to retrieve a list of common movies for two existing actors 
		assertEquals(1, mt.getCommonMovie("meryl streep", "tom hanks", movieDB.getActorsInfo()).size(),
				"\"tom hanks\" and \"meryl streep\" should have 1 movie in common.");
		assertTrue(mt.getCommonMovie("meryl streep", "tom hanks", movieDB.getActorsInfo()).contains("the post"),
				"\"the post\" should be a common movie between \"tom hanks\" and \"meryl streep\".");

		// try to retrieve a list of common movies for two existing actors
		assertEquals(1, mt.getCommonMovie("meryl streep", "amy adams", movieDB.getActorsInfo()).size(),
				"\"meryl streep\" and \"amy adams\" should have 1 movie in common.");
		assertTrue(mt.getCommonMovie("meryl streep", "amy adams", movieDB.getActorsInfo()).contains("doubt"),
				"\"doubt\" should be a common movie between \"meryl streep\" and \"amy adams\".");
		
		// try to retrieve a list of common movies for two identical actors
		assertEquals(3, mt.getCommonMovie("meryl streep", "meryl streep", movieDB.getActorsInfo()).size(),
				"\"meryl streep\" and \"meryl streep\" should have 3 movies in common. The actor names are identical, so this should return a list of movies the actor was in.");
		assertTrue(mt.getCommonMovie("meryl streep", "meryl streep", movieDB.getActorsInfo()).contains("doubt"),
				"\"doubt\" should be a common movie between \"meryl streep\" and \"meryl streep\".");
		assertTrue(mt.getCommonMovie("meryl streep", "meryl streep", movieDB.getActorsInfo()).contains("sophie's choice"),
				"\"sophie's choice\" should be a common movie between \"meryl streep\" and \"meryl streep\".");
		assertTrue(mt.getCommonMovie("meryl streep", "meryl streep", movieDB.getActorsInfo()).contains("the post"),
				"\"the post\" should be a common movie between \"meryl streep\" and \"meryl streep\".");
		
		// try to retrieve a list of common movies for actors with leading/trailing whitespace
		assertEquals(1, mt.getCommonMovie("   meryl streep  ", "    amy adams", movieDB.getActorsInfo()).size(),
				"\"   meryl streep  \" and \"    amy adams\" should have 1 movie in common. Leading and trailing whitespace should be ignored from the name.");
		assertTrue(mt.getCommonMovie("meryl streep", "    amy adams", movieDB.getActorsInfo()).contains("doubt"),
				"\"doubt\" should be a common movie between \"   meryl streep  \" and \"    amy adams\". Leading and trailing whitespace should be ignored from the name.");
		
		// try to retrieve a list of common movies for actors with different casing
		assertEquals(1, mt.getCommonMovie("mERYl strEEp", "TOM HANKs", movieDB.getActorsInfo()).size(),
				"\"TOM HANKs\" and \"mERYl strEEp\" should have 1 movie in common, because the name should be case insensitive.");
		assertTrue(mt.getCommonMovie("mERYl strEEp", "TOM HANKs", movieDB.getActorsInfo()).contains("the post"),
				"\"the post\" should be a common movie between \"TOM HANKs\" and \"mERYl strEEp\". The name should be case insensitive.");
		
		// try to retrieve a list of common movies for nonexistent actors 
		assertEquals(0, mt.getCommonMovie("meryl streep", "nonexistent actor", movieDB.getActorsInfo()).size(),
				"\"meryl streep\" and \"nonexistent actor\" should have 0 movies in common. An empty list should be returned");
		assertFalse(mt.getCommonMovie("meryl streep", "nonexistant actor", movieDB.getActorsInfo()).contains("the post"),
				"\"the post\" should not be a common movie between \"meryl streep\" and \"nonexistant actor\".");
	}

	@Test
	void testGoodMovies() {
		
		// try to retrieve the list of movie names that both critics and audiences have rated >= 85
		assertEquals(3, mt.goodMovies(movieDB.getMoviesInfo()).size(),
				"There should be 3 movies that are considered good movies, movies with both critics and audience rating that are greater than or equal to 85.");
		assertTrue(mt.goodMovies(movieDB.getMoviesInfo()).contains("jaws"),
				"\"jaws\" should be considered a good movie, since it's critics and audience ratings are both greater than or equal to 85.");
		assertTrue(mt.goodMovies(movieDB.getMoviesInfo()).contains("rocky ii"),
				"\"rocky ii\" should be considered a good movie, since it's critics and audience ratings are both greater than or equal to 85.");
		assertTrue(mt.goodMovies(movieDB.getMoviesInfo()).contains("et"),
				"\"et\" should be considered a good movie, since it's critics and audience ratings are both greater than or equal to 85.");
		
		// ensure movies with audience and critic ratings below 85 are not included in the list of good movies returned 
		assertFalse(mt.goodMovies(movieDB.getMoviesInfo()).contains("doubt"),
				"\"doubt\" should not be considered a good movie, since it's critics and audience ratings are both lower than 85.");
		assertFalse(mt.goodMovies(movieDB.getMoviesInfo()).contains("seven"),
				"\"seven\" should not be considered a good movie, since it's critics and audience ratings are both lower than 85.");
		assertFalse(mt.goodMovies(movieDB.getMoviesInfo()).contains("popeye"),
				"\"popeye\" should not be considered a good movie, since it's critics and audience ratings are both lower than 85.");
		
		// ensure nonexisting movies are not added to the list of good movies
		assertFalse(mt.goodMovies(movieDB.getMoviesInfo()).contains("nonexistent movie"),
				"\"nonexistent movie\" should not be included in the list of movies with critics and audience ratings greater than or equal to 85.");
		
		// ensure movies where only the critic's rating is >= 85 is not included
		assertFalse(mt.goodMovies(movieDB.getMoviesInfo()).contains("arrival"),
				"\"arrival\" should not be included in the list of good movies because only the critic rating is >= 85. Both the audience and critic rating need to be >= 85 for the movie to be included.");
		

		
	}

	@Test
	void testGetCommonActors() {
		
		// try to retrieve a list of common actors for two existing movies
		assertEquals(1, mt.getCommonActors("doubt", "the post", movieDB.getActorsInfo()).size(),
				"There should be one actor that appeared in both \"doubt\" and \"the post\".");
		assertTrue(mt.getCommonActors("doubt", "the post", movieDB.getActorsInfo()).contains("meryl streep"),
				"The actor that appeared in both \"doubt\" and \"the post\" should be \"meryl streep\".");
		assertEquals(2, mt.getCommonActors("catch me if you can", "the post", movieDB.getActorsInfo()).size(),
				"There should be one actor that appeared in both \"doubt\" and \"the post\".");
		assertTrue(mt.getCommonActors("catch me if you can", "the post", movieDB.getActorsInfo()).contains("tom hanks"),
				"Tom Hanks appears in both \"catch me if you can\" and \"the post\".");
		assertTrue(mt.getCommonActors("catch me if you can", "the post", movieDB.getActorsInfo()).contains("pretend actor"),
				"Pretend Actor appears in both \"catch me if you can\" and \"the post\".");
		
		// try to retrieve a list of common actors for two identical movies
		assertEquals(3, mt.getCommonActors("the post", "the post", movieDB.getActorsInfo()).size(),
				"\"the post\" and \"the post\" should have 3 actors in common. The movie names are identical, so this should return a list of actors who acted in the movie.");
		assertTrue(mt.getCommonActors("the post", "the post", movieDB.getActorsInfo()).contains("meryl streep"),
				"\"mery streep\" should be a common movie between \"the post\" and \"the post\".");
		assertTrue(mt.getCommonActors("the post", "the post", movieDB.getActorsInfo()).contains("tom hanks"),
				"\"tom hanks\" should be a common movie between \"the post\" and \"the post\".");
		assertTrue(mt.getCommonActors("the post", "the post", movieDB.getActorsInfo()).contains("pretend actor"),
				"\"pretend actor\" should be a common movie between \"the post\" and \"the post\".");
		
		// try to retrieve a list of common actors for movies with leading/trailing whitespace
		assertEquals(1, mt.getCommonActors("   doubt  ", "    the post  ", movieDB.getActorsInfo()).size(),
				"\"   doubt  \" and \"    the post  \" should have 1 actor in common. Leading and trailing whitespace should be ignored from the movie name.");
		assertTrue(mt.getCommonActors("   doubt  ", "    the post  ", movieDB.getActorsInfo()).contains("meryl streep"),
				"\"meryl streep\" should be a common actor between \"   doubt  \" and \"    the post  \". Leading and trailing whitespace should be ignored from the movie name.");
		
		// try to retrieve a list of common actors for movies with different casing
		assertEquals(1, mt.getCommonActors("DouBt", "THE pOST", movieDB.getActorsInfo()).size(),
				"\"DouBt\" and \"THE pOST\" should have 1 actor in common, because the movie name should be case insensitive.");
		assertTrue(mt.getCommonActors("DouBt", "THE pOST", movieDB.getActorsInfo()).contains("meryl streep"),
				"\"meryl streep\" should be a common actor between \"DouBt\" and \"THE pOST\". The movie name should be case insensitive.");
		
		// try to retrieve a list of common actors for nonexistent movies
		assertEquals(0, mt.getCommonActors("doubt", "nonexistent movie", movieDB.getActorsInfo()).size(),
				"\"doubt\" and \"nonexistent movie\" should have 0 actors in common. An empty list should be returned");
		assertFalse(mt.getCommonActors("doubt", "nonexistant movie", movieDB.getActorsInfo()).contains("meryl streep"),
				"\"meryl streep\" should not be a common movie between \"doubt\" and \"nonexistant movie\" because an empty list of actors should have been returned.");
	}

	@Test
	void testGetMean() {
		
		// Try to get the mean of all critics ratings (with 0.1 delta)
		assertEquals(67.9, mt.getMean(movieDB.getMoviesInfo())[0], 0.1, "The mean of all critics ratings is incorrect.");
		
		// Try to get the mean of all audience ratings (with 0.1 delta)
		assertEquals(65.7, mt.getMean(movieDB.getMoviesInfo())[1], 0.1, "The mean of all audience ratings is incorrect.");
		
		// Try to insert a new rating and get the mean of all critics ratings
		mt.insertRating("testmovie", new int[] { 34, 99 }, movieDB.getMoviesInfo());
		assertEquals(63.6, mt.getMean(movieDB.getMoviesInfo())[0], 0.1, "The mean of all critics ratings is incorrect after inserting a new rating.");
		
		// Try to insert a new rating and get the mean of all audience ratings
		assertEquals(69.9, mt.getMean(movieDB.getMoviesInfo())[1], 0.1, "The mean of all audience ratings is incorrect after inserting a new rating.");

	}
	
	@Test
	void testNormalizeString() {
		
		// try to make a string with different casing all lowercase
		assertEquals("normalizedstring", mt.normalizeString("nORMAlizEDStriNG"),
				"The normalizeString() method should return a lowercase string when a string with different casing is given as a parameter.");
		
		// try to remove leading and trailing whitespace from a string
		assertEquals("normalizedstring", mt.normalizeString("   normalizedstring    "),
				"The normalizeString() method should return a string with leading and trailing whitespace removed.");
		
		// try to make a string all lowercase while also removing leading and trailing whitespace
		assertEquals("normalizedstring", mt.normalizeString("   nORMALIZeDString "),
				"The normalizeString() method should return a lowercase string eith leading and trailing whitespace removed.");
		
	}
	
	@Test
	void testObjectFromActorString() {
		
		// try to get existing actor object from given actor string
		assertEquals(movieDB.getActorsInfo().get(0), mt.actorObjectFromActorString("Meryl Streep", movieDB.getActorsInfo()),
				"The actorObjectFromActorString() method should return the actor object associated with the actor's name.");
		
		// try to get nonexisting actor object from actor string (should return null)
		assertEquals(null, mt.actorObjectFromActorString("nonexistent actor", movieDB.getActorsInfo()),
				"The actorObjectFromActorString() method should return null if the given actor's name does not exist in the list of actors.");
		
		// try to get existing actor object from string with different casing
		assertEquals(movieDB.getActorsInfo().get(0), mt.actorObjectFromActorString("MERYL StrEEp", movieDB.getActorsInfo()),
				"The actorObjectFromActorString() method should return the actor object associated with the actor's name. The actor's name should be case insensitive.");
		
		// try to get existing actor object from string with leading and trailing whitespace
		assertEquals(movieDB.getActorsInfo().get(0), mt.actorObjectFromActorString("     Meryl Streep ", movieDB.getActorsInfo()),
				"The actorObjectFromActorString() method should return the actor object associated with the actor's name, discluding leading and trailing whitespace.");
		
	}
}
