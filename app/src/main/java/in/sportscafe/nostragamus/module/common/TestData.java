package in.sportscafe.nostragamus.module.common;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

import in.sportscafe.nostragamus.module.feed.dto.Match;
import in.sportscafe.nostragamus.module.play.prediction.dto.Question;
import in.sportscafe.nostragamus.webservice.MyWebService;

/**
 * Created by Jeeva on 17/6/16.
 */
public class TestData {

    public static List<Question> generateDummyQuestions() {
        String data = "[\n" +
                "  {\n" +
                "    \"question_id\": 1,\n" +
                "    \"match_id\": 1,\n" +
                "    \"question_text\": \"Who will hit more sixes in this match?\",\n" +
                "    \"question_context\": \"ABD has been consistent with 18 sixes so far. Gayle is having a poor run in this tournament, but we know what he is capable with the record for maximum sixes in the history of the IPL and all T20 cricket. \",\n" +
                "    \"question_option_1\": \"Gayle\",\n" +
                "    \"question_image_1\": \"http://i3.mirror.co.uk/incoming/article7122647.ece/ALTERNATES/s615b/Chris-Gayle.jpg\",\n" +
                "    \"question_option_2\": \"AB De Villiers\",\n" +
                "    \"question_image_2\": \"http://arysports.tv/wp-content/uploads/2015/12/AB_AFP_File_0_0_0_0.jpg\",\n" +
                "    \"question_live\": 1\n" +
                "  },\n" +
                "  {\n" +
                "    \"question_id\": 2,\n" +
                "    \"match_id\": 1,\n" +
                "    \"question_text\": \"How many wickets will Sunil Narine take?\",\n" +
                "    \"question_context\": \"Sunil Narine is back but is not among the wickets as usual this IPL with best figures of 2/21 so far and an overall tally of 10 wickets in 7 games, very low for his standards. \",\n" +
                "    \"question_option_1\": \"2 wicket or less\",\n" +
                "    \"question_image_1\": \"http://ste.india.com/sites/default/files/2016/06/04/495602-sunil-narine-warm-up.jpg\",\n" +
                "    \"question_option_2\": \"More than 2 wickets\",\n" +
                "    \"question_image_2\": \"http://ste.india.com/sites/default/files/2015/03/30/340983-kkr-narine-clb-700.jpg\",\n" +
                "    \"question_live\": 1,\n" +
                "    \"question_answer\": 1\n" +
                "  },\n" +
                "  {\n" +
                "    \"question_id\": 3,\n" +
                "    \"match_id\": 1,\n" +
                "    \"question_text\": \"Will Kohli fire yet again? How many will he score?\",\n" +
                "    \"question_context\": \"The law of averages has to catch up with him soon.\",\n" +
                "    \"question_option_1\": \"Less than 30 runs\",\n" +
                "    \"question_image_1\": \"http://www.viratkohli.website/wp-content/uploads/2015/04/freely-says-virat-kohli-00923.jpg\",\n" +
                "    \"question_option_2\": \"More than 30 runs\",\n" +
                "    \"question_image_2\": \"http://s.ndtvimg.com/images/content/2016/mar/806/virat-kohli-fist-world-t20.jpg?downsize=764:573&output-quality=80&output-format=jpg\",\n" +
                "    \"question_live\": 1,\n" +
                "    \"question_answer\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"question_id\": 4,\n" +
                "    \"match_id\": 1,\n" +
                "    \"question_text\": \"Which leggie will have the better economy rate?\",\n" +
                "    \"question_context\": \"The Chacha brothers, Chahal and Chawla have been consistent performers who have played vital roles this season for their teams. Chawla will likely have three lefties to bowl at though.\",\n" +
                "    \"question_option_1\": \"Yuzvendra Chahal\",\n" +
                "    \"question_image_1\": \"http://img01.ibnlive.in/ibnlive/uploads/2016/05/Yuzvendra-Chahal-BCCI.jpg\",\n" +
                "    \"question_option_2\": \"Piyush Chawla\",\n" +
                "    \"question_image_2\": \"http://www.liveindia.com/cricket/piyush-chawla4.jpg\",\n" +
                "    \"question_live\": 1,\n" +
                "    \"question_answer\": 1\n" +
                "  },\n" +
                "  {\n" +
                "    \"question_id\": 5,\n" +
                "    \"match_id\": 2,\n" +
                "    \"question_text\": \"Will David Warner score more than 30% of his teams runs?\",\n" +
                "    \"question_context\": \"He has done that for 7 of 8 games this season.\",\n" +
                "    \"question_option_1\": \"You have been Warnered\",\n" +
                "    \"question_image_1\": \"http://cricfrenzy.com/wp-content/uploads/2016/03/David-Warner-Cricinfo-Biography-Profile-Records-Highlights-2.jpg\",\n" +
                "    \"question_option_2\": \"Warner out\",\n" +
                "    \"question_image_2\": \"http://s.ndtvimg.com/images/content/2014/oct/806/david-warner-t20i-uae.jpg\",\n" +
                "    \"question_live\": 1,\n" +
                "    \"question_answer\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"question_id\": 6,\n" +
                "    \"match_id\": 2,\n" +
                "    \"question_text\": \"Will Suresh Raina finally fire with a 30+ score?\",\n" +
                "    \"question_context\": \"Touted as the the best IPL batsman thus far, he has been struggling for form this year. Maybe marriage has something to do with it?\",\n" +
                "    \"question_option_1\": \"Raina wins\",\n" +
                "    \"question_image_1\": \"http://www.forcelebrities.com/wp-content/uploads/2016/03/Suresh-Raina-forcelebrities.com2_.jpg\",\n" +
                "    \"question_option_2\": \"Raina out\",\n" +
                "    \"question_image_2\": \"http://brightcove04.o.brightcove.com/3910869736001/3910869736001_4814193124001_4814154630001-vs.jpg?pubId=3910869736001\",\n" +
                "    \"question_live\": 1,\n" +
                "    \"question_answer\": 0\n" +
                "  },\n" +
                "  {\n" +
                "    \"question_id\": 7,\n" +
                "    \"match_id\": 6,\n" +
                "    \"question_text\": \"Who will hit more aces?\",\n" +
                "    \"question_context\": \"Both men have powerful serves, but Wawrinka has the edge with 80% first serve in.\",\n" +
                "    \"question_option_1\": \"Wawrinka\",\n" +
                "    \"question_image_1\": \"http://i2.cdn.turner.com/cnnnext/dam/assets/160123023903-wawrinka-shirt-super-169.jpg\",\n" +
                "    \"question_option_2\": \"Djokovic\",\n" +
                "    \"question_image_2\": \"http://www.ausopen.com/images/pics/misc/g_200116_djokovic_bs_22.jpg\",\n" +
                "    \"question_live\": 1,\n" +
                "    \"question_answer\": 1\n" +
                "  },\n" +
                "  {\n" +
                "    \"question_id\": 8,\n" +
                "    \"match_id\": 6,\n" +
                "    \"question_text\": \"Will the game go into 5 sets?\",\n" +
                "    \"question_context\": \"Djoker has made a habit of taking it to 5 setters this Wimbledon while the Fed has been finishing things within 3 with consistency.\",\n" +
                "    \"question_option_1\": \"5 setter!\",\n" +
                "    \"question_image_1\": \"https://mayfairclubs.files.wordpress.com/2012/01/gamemakers-1-roger-federer-0-us-open-2011-another-tournament-falls-prey-tennis-special-967291.jpg\",\n" +
                "    \"question_option_2\": \"No 5 sets\",\n" +
                "    \"question_image_2\": \"http://ichef.bbci.co.uk/onesport/cps/480/mcs/media/images/76034000/jpg/_76034651_147932460.jpg\",\n" +
                "    \"question_live\": 1,\n" +
                "    \"question_answer\": 0\n" +
                "  }\n" +
                "]";
        return MyWebService.getInstance().getObjectFromJson(data,
                new TypeReference<List<Question>>() {
                });
    }

    public static List<Match> generateDummyMatches() {
        String data = "[\n" +
                " {\n" +
                "   \"match_id\": 1,\n" +
                "   \"tournament_id\": 1,\n" +
                "   \"tournament_name\": \"Indian Premier League\",\n" +
                "   \"match_stage\": \"Semifinals\",\n" +
                "   \"match_venue\": \"Pune\",\n" +
                "   \"match_parties\": \"KKR vs RCB\",\n" +
                "   \"match_starttime\": \"2016-06-19T06:31:19.000Z\",\n" +
                "   \"match_endtime\": \"2016-06-19T15:00:00.000Z\",\n" +
                "   \"match_result\": null,\n" +
                "   \"match_questions_live\": 1\n" +
                " },\n" +
                " {\n" +
                "   \"match_id\": 2,\n" +
                "   \"tournament_id\": 1,\n" +
                "   \"tournament_name\": \"Indian Premier League\",\n" +
                "   \"match_stage\": \"Semifinals\",\n" +
                "   \"match_venue\": \"Bangalore\",\n" +
                "   \"match_parties\": \"GL vs SRH\",\n" +
                "   \"match_starttime\": \"2016-06-19T09:00:00.000Z\",\n" +
                "   \"match_endtime\": \"2016-06-19T17:00:00.000Z\",\n" +
                "   \"match_result\": null,\n" +
                "   \"match_questions_live\": 1\n" +
                " },\n" +
                " {\n" +
                "   \"match_id\": 3,\n" +
                "   \"tournament_id\": 1,\n" +
                "   \"tournament_name\": \"Indian Premier League\",\n" +
                "   \"match_stage\": \"Group\",\n" +
                "   \"match_venue\": \"Bangalore\",\n" +
                "   \"match_parties\": \"RCB vs SRH\",\n" +
                "   \"match_starttime\": \"2016-06-15T09:00:00.000Z\",\n" +
                "   \"match_endtime\": \"2016-06-15T17:00:00.000Z\",\n" +
                "   \"match_result\": \"RCB won by 2 wickets\",\n" +
                "   \"match_questions_live\": 1\n" +
                " },\n" +
                " {\n" +
                "   \"match_id\": 4,\n" +
                "   \"tournament_id\": 1,\n" +
                "   \"tournament_name\": \"Indian Premier League\",\n" +
                "   \"match_stage\": \"Group\",\n" +
                "   \"match_venue\": \"Bangalore\",\n" +
                "   \"match_parties\": \"DD vs RPS\",\n" +
                "   \"match_starttime\": \"2016-06-15T09:00:00.000Z\",\n" +
                "   \"match_endtime\": \"2016-06-15T17:00:00.000Z\",\n" +
                "   \"match_result\": \"RPS won by 21 runs\",\n" +
                "   \"match_questions_live\": 1\n" +
                " },\n" +
                " {\n" +
                "   \"match_id\": 5,\n" +
                "   \"tournament_id\": 2,\n" +
                "   \"tournament_name\": \"Wimbledon\",\n" +
                "   \"match_stage\": \"Qualifiers\",\n" +
                "   \"match_venue\": \"London\",\n" +
                "   \"match_parties\": \"Federer vs Nadal\",\n" +
                "   \"match_starttime\": \"2016-06-16T09:00:00.000Z\",\n" +
                "   \"match_endtime\": \"2016-06-16T17:00:00.000Z\",\n" +
                "   \"match_result\": \"Federer won 6-3,4-6,7-5\",\n" +
                "   \"match_questions_live\": 1\n" +
                " },\n" +
                " {\n" +
                "   \"match_id\": 6,\n" +
                "   \"tournament_id\": 2,\n" +
                "   \"tournament_name\": \"Wimbledon\",\n" +
                "   \"match_stage\": \"Semifinals\",\n" +
                "   \"match_venue\": \"London\",\n" +
                "   \"match_parties\": \"Djokovic vs Wawrinka\",\n" +
                "   \"match_starttime\": \"2016-06-15T09:00:00.000Z\",\n" +
                "   \"match_endtime\": \"2016-06-15T17:00:00.000Z\",\n" +
                "   \"match_result\": \"Wawrinka won 6-4,6-4,6-3\",\n" +
                "   \"match_questions_live\": 1\n" +
                " },\n" +
                " {\n" +
                "   \"match_id\": 7,\n" +
                "   \"tournament_id\": 2,\n" +
                "   \"tournament_name\": \"Wimbledon\",\n" +
                "   \"match_stage\": \"Finals\",\n" +
                "   \"match_venue\": \"London\",\n" +
                "   \"match_parties\": \"Djokovic vs Federer\",\n" +
                "   \"match_starttime\": \"2016-06-19T09:00:00.000Z\",\n" +
                "   \"match_endtime\": \"2016-06-19T17:00:00.000Z\",\n" +
                "   \"match_result\": null,\n" +
                "   \"match_questions_live\": 1\n" +
                " }\n" +
                "]";
        return MyWebService.getInstance().getObjectFromJson(data,
                new TypeReference<List<Match>>() {
                });
    }

}