package automation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import preprocess.TimeNormalizer;
import tag.JobNameTagging;
import tag.TagGenerator;
import file.FileReader;
import file.FileWriter;

public class Automator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileReader reader = new FileReader();
		FileWriter writer = new FileWriter();
		TimeNormalizer normalizer = new TimeNormalizer();
		List<String> normJobList = new ArrayList<String>();

		// 1 Split SQL and non-SQL jobs
		// 2 Read job names
		int jobColumn = reader.searchTargetColumn(
				SystemParamater.NON_SQL_FILE_NAME, "jobName");
		List<String> jobList = reader.getColumnCellList(
				SystemParamater.NON_SQL_FILE_NAME, jobColumn);

		// 3 timestamp normalize
		if (SystemParamater.NORMALIZATION_REQUEST == true) {
			normJobList = normalizer.getTimeNormalizedJobList(jobList);
			writer.writeColumnBasedOnSource(SystemParamater.NON_SQL_FILE_NAME,
					SystemParamater.NORM_FILE_NAME, "normName", normJobList,
					jobColumn, 3);
		}
		// 4 Generate tag
		Map<String, List<String>> userJobNameInput = reader.getUserJobNameMap();
		TagGenerator tagGenerator = new TagGenerator(userJobNameInput);
		//5 Tagging
		JobNameTagging tagger = new JobNameTagging(tagGenerator.getUserTag(), tagGenerator.getUserTokenInfo());
		tagger.addTag();
		JobNameTagging.outputTokenInfo(tagGenerator);
	}
}
