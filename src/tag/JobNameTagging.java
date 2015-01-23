package tag;

import instance.TokenComparator;
import instance.TokenInfo;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import automation.SystemParamater;
import token.TokenGenerator;
import file.FileReader;
import file.FileWriter;

public class JobNameTagging {

	private Map<String, List<String>> taglib;
	private Map<String, List<TokenInfo>> tokenlib;
	private Map<String, List<String>> userJobName;

	private FileReader reader;
	private FileWriter writer;

	public JobNameTagging(Map<String, List<String>> inputTaglib,
			Map<String, List<TokenInfo>> inputTokenInfo) {
		taglib = new HashMap<String, List<String>>(inputTaglib);
		reader = new FileReader();
		writer = new FileWriter();
		userJobName = new HashMap<String, List<String>>();
		tokenlib = new HashMap<String, List<TokenInfo>>(inputTokenInfo);
	}

	public void addTag() {

		List<String> taggedjobList = new ArrayList<String>();

		// 1 get user ,job name list from NORM_FILE(The file should be ordered
		// by user, jobName)
		// generate user-job map
		userJobName = getUserJobNameMap();

		// 2 for each user
		// for each job name, compute the tag result
		// a list<tag> for each job
		// generate a whole tagged job list for all jobs
		for (String user : userJobName.keySet()) {
			List<String> currentJobList = userJobName.get(user);
			if (currentJobList == null)
				continue;
			for (String jobName : currentJobList) {
				taggedjobList.add(getTaggedjobName(user, jobName));
			}
		}
		// 4 copy the NORM_FILE workbook and add the tags in a new column
		// write into TAG_FILE
		int columnNo = reader.searchTargetColumn(
				SystemParamater.NORM_FILE_NAME, "normName");
		writer.writeColumnBasedOnSource(SystemParamater.NORM_FILE_NAME,
				SystemParamater.TAG_FILE_NAME, "tagName", taggedjobList,
				columnNo, 1);

	}

	private Map<String, List<String>> getUserJobNameMap() {
		Map<String, List<String>> mapUserJobName = new LinkedHashMap<String, List<String>>();
		int jobColumnNo = reader.searchTargetColumn(
				SystemParamater.NORM_FILE_NAME, "normName");
		int userColumnNo = reader.searchTargetColumn(
				SystemParamater.NORM_FILE_NAME, "user");
		List<String> jobList = reader.getColumnCellList(
				SystemParamater.NORM_FILE_NAME, jobColumnNo);
		// jobList.remove(0);
		List<String> userList = reader.getColumnCellList(
				SystemParamater.NORM_FILE_NAME, userColumnNo);
		// userList.remove(0);
		assert (jobList.size() == userList.size());
		ListIterator<String> jobIter = jobList.listIterator();
		ListIterator<String> userIter = userList.listIterator();
		List<String> currentJobList = new ArrayList<String>();

		int jobCount = 0;
		for (int index = 0; index != jobList.size(); ++index) {

			String currentUser = (String) userIter.next();
			if (userIter.hasNext()) {
				String nextUser = (String) userIter.next();
				userIter.previous();

				if (currentUser.equals(nextUser)) {
					currentJobList.add((String) jobIter.next());
				} else {
					currentJobList.add((String) jobIter.next());
					mapUserJobName.put(currentUser, new ArrayList(
							currentJobList));

					jobCount += currentJobList.size();
					currentJobList.clear();
				}
			} else {
				currentJobList.add((String) jobIter.next());
				mapUserJobName.put(currentUser, new ArrayList(currentJobList));
				jobCount += currentJobList.size();
			}
		}
		return mapUserJobName;
	}

	// Use user-tag lib to describe job names
	// If no tags found, use the original job name
	private String getTaggedjobName(String user, String jobName) {
		String tagResult = new String();
		List<String> resultTagList = new ArrayList<String>();

		if (taglib.get(user) != null) {
			// 1 tokenize the job name
			TokenGenerator tg = new TokenGenerator();
			List<String> tokenList = tg.generateTokenList(jobName);
			// 2 if one tag is found N times, use N tags to describe

			int index = 0;
			for (String token : tokenList) {
				if (SystemParamater.FIRST_TOKEN_AS_TAG == true) {
					if (index++ == 0) {
						resultTagList.add(new String(token));
						continue;
					}
				}
				if (Collections.frequency(taglib.get(user), token) > 0) {
					resultTagList.add(new String(token));
				}
			}
			if (resultTagList.isEmpty()) {
				// No tags in for this user

				List<TokenInfo> tokenList_1 = new ArrayList<TokenInfo>(
						tokenlib.get(user));
				List<TokenInfo> tokenList1 = new ArrayList<TokenInfo>();
				for(TokenInfo t1: tokenList_1){
					for(String s1: tokenList){
						if(t1.getToken().equals(s1))
							tokenList1.add(t1);
					}	
				}
				TokenComparator comparator = new TokenComparator();
				Collections.sort(tokenList1, comparator);
				for (int i = 0; i != SystemParamater.MAX_TOKEN_FOR_UNTAG_JOB
						&& i != tokenList1.size(); ++i) {
					if (i != 0) {
						tagResult += " ";
					}
					
					if (Collections.frequency(tokenList,tokenList1.get(i).getToken() ) > 0) {
						tagResult += tokenList1.get(i).getToken();
					}
					
				}
				return tagResult;
				// return jobName;
			} else {
				for (int i = 0; i != resultTagList.size(); ++i) {
					if (i != 0) {
						tagResult += " ";
					}
					tagResult += resultTagList.get(i);
				}
			}
		} else {

			tagResult = " ";
		}

		return tagResult;
	}

	private int tagOccurenceCount(String jobName, String tag) {

		TokenGenerator tg = new TokenGenerator();

		List<String> tokenList = tg.generateTokenList(jobName);
		/*
		 * while ((idx = jobName.indexOf(tag, idx)) != -1) { idx++; count++; //
		 * guo return count; }
		 */
		int result = Collections.frequency(tokenList, tag);
		return result;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<String, List<String>> myTagLib = new HashMap();

		FileReader reader = new FileReader();
		Map<String, List<String>> userJobNameInput = reader.getUserJobNameMap();
		TagGenerator test = new TagGenerator(userJobNameInput);

		JobNameTagging tagger = new JobNameTagging(test.getUserTag(),
				test.getUserTokenInfo());

		// List<String> list = test.getUserTag().get("b_bis");

		tagger.addTag();
		outputTokenInfo(test);
	}

	public static void outputTokenInfo(TagGenerator test) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream(
									"C:\\Users\\Richard\\Desktop\\EBAY DATA\\" + SystemParamater.cluster_name+"\\token.csv",
									false)));
			for (String user : test.getUserTokenInfo().keySet()) {

				for (TokenInfo info : test.getUserTokenInfo().get(user)) {
					out.write(user + ",");
					out.write(info.getToken() + ",");
					out.write(info.getTF() + ",");
					out.write(info.getIUF() + ",");
					out.write(info.getTF() * info.getIUF() + ",");
					out.newLine();
				}
				// out.newLine();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// test.getUserTokenInfo();
	}

}
