package preprocess;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeNormalizer {

	// public List<String> targetJobName;
	//public List<String> nonSQLJobList;
	// public List<String> sqlJobList;
	public List<String> resultJobName;

	// Normalizing rules
	public List<Map<String, String>> timeRule;

	// public List<Map<String, String>> idRule;
	// public List<Map<String, String>> fileRule;
	// public List<Map<String, String>> sqlTimeRule;

	// public List<Map<String, String>> timeRule;
	public TimeNormalizer() {

	//	/nonSQLJobList = new ArrayList<String>();

		resultJobName = new ArrayList<String>();
	}

	/*
	 * public void clean(List<String> target) { //
	 * [EDBBECFA3CB349659F239337553C444E/59EC38DA1BE2450F92802DC7A57AA7A5] //
	 * com.ebay.pandaren.report.TestSummaryReport/(6/6) // -> //
	 * com.ebay.pandaren.report.TestSummaryReport/(6/6) ListIterator<String>
	 * iter = target.listIterator(); while (iter.hasNext()) { String str =
	 * (String) iter.next(); // str.replaceAll("\[\]",""); //
	 * iter.set(str.replaceAll("\\(([^(]*)\\)",""));
	 * iter.set(str.replaceFirst("\\[([^(]*)\\] ", "")); } }
	 */

	public String timeNormalize(String jobName) {

		jobName = jobName.replaceFirst("\\[([^(]*)\\] ", "");
		
		// /2014/03/29/00
		jobName = jobName.replaceAll(
				"20[0-9]{2}\\/[0-9]{2}\\/[0-9]{2}\\/[0-9]{2}", "~");
		jobName = jobName.replaceAll(
				"20[0-9]{2}\\/[0-9]{2}\\/[0-9]{2}\\/[0-9]{2}\\/[0-9]{2}", "~");
		jobName = jobName.replaceAll("20[0-9]{2}\\/[0-9]{2}\\/[0-9]{2}", "~");

		jobName = jobName
				.replaceAll(
						"20[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}\\_[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}",
						"~");

		jobName = jobName.replaceAll("[0-9]{4}\\-[0-9]{6}\\-*[0-9]{7,10}\\-",
				"~");
		// jobName =
		// jobName.replaceAll("[0-9]{4}\\-[0-9]{6}\\-*[0-9]{7,8}","~");

		jobName = jobName.replaceAll("\\.\\.\\.[0-9]{3}\\/", "...\\/~");
		jobName = jobName.replaceAll("20[0-9]{2}\\-[0-9]{2}\\-[0-9]{2}", "~");
		jobName = jobName.replaceAll("[0-9]{2}\\-[0-9]{2}\\-[0-9]{2}", "~");
		jobName = jobName.replaceAll("20[0-9]{6}\\/", "~");
		jobName = jobName.replaceAll("20[0-9]{6}", "~");

		return jobName;
	}

/*	public String idNormalize(String jobName) {
		
			jobName = jobName.replaceAll("yosemite-eod.[(A-Za-z0-9]{10}",
					"yosemite-eod.-");

		}
		return jobName;
	}
*/
	/*public String fileNormalize(String jobName) {
		if (!isSQLJob(jobName)) {

			jobName = jobName
					.replaceAll(
							"\\/[a-z0-9]{8}\\-[a-z0-9]{4}\\-[a-z0-9]{4}\\-[a-z0-9]{4}\\-[a-z0-9]{12}",
							"\\/-");
			jobName = jobName
					.replaceAll(
							"\\/[a-z0-9]{8}\\-[a-z0-9]{4}\\-[a-z0-9]{4}\\-[a-z0-9]{4}\\-[a-z0-9]{6}\\.\\.\\.",
							"\\/-");

		}
		return jobName;
	}*/




	public List<String> getTimeNormalizedJobList(List<String> target) {
		resultJobName = target;

		
		// For nonSQl names
		ListIterator<String> iter = resultJobName.listIterator();
		while (iter.hasNext()) {
			String name = (String) iter.next();
			String result = timeNormalize(name);
			
			if (result != name) {
				iter.set(result);
			}
		}
		
		return resultJobName;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Normalizer normalizer = new Normalizer();
		List<String> test = new ArrayList<String>();
		test.add("select " + "\n"
				+ " concat_ws('|',extid.idt...string))(Stage-1)");
		test.add("AD-CONTAINER : JobOptions{forceCleanup=true, compressOutput=true, numReducers=400, numMappers=-1, output='hdfs://apollo-phx-nn.vip.ebay.com:8020/apps/hdmi-paypal/dgdeploy/AdContainer/working/0545a5cf-34f6-4b82-80da-932b541c6a5c, input='[hdfs://apollo-phx-nn.vip.ebay.com:8020/apps/hdmi-paypal/dgdeploy/raw/ads/2014/03/30/22, hdfs://apollo-phx-nn.vip.ebay.com:8020/apps/hdmi-paypal/dgdeploy/raw/olp/2014/03/30/22], avro-input-files='[hdfs://apollo-phx-nn.vip.ebay.com:8020/apps/hdmi-paypal/dgdeploy/AdContainer/active/incomplete/2014/03/30/21], avroOutput=false, jsonInputSequenceFile=false, timePoint=2014/03/30/22, params='null}");
		test.add("Bot11Job sessionIn={/user/b_qe_coe/tmp.yosemite-eod.IS17bUTJyp/open_session,/user/b_qe_coe/yosemite-regression/output/yosemite-branch-Feat-NewBots-by-xiaokwu-at-~/intraday-fresh/*/session} botSessionOut=/user/b_qe_coe/tmp.yosemite-eod.IS17bUTJyp/bot-Bot11Job config=yosemite-branch-Feat-NewBots-by-xiaokwu-at-~/etc/ubi.properties test");
		test.add("(EDBBECFA3CB349659F239337553C444E/59EC38DA1BE2450F92802DC7A57AA7A5) com.ebay.pandaren.report.TestSummaryReport/(6/6)");
		test.add("[EDBBECFA3CB349659F239337553C444E/59EC38DA1BE2450F92802DC7A57AA7A5] com.ebay.pandaren.report.TestSummaryReport/(6/6)");
		test.add("INSERT OVERWRITE TABLE ecn_dw.PROC_D...B.IP)(Stage-4)");
		test.add("SNAPSHOT FILE GENERATOR input=/sys/edw//ngdf_fdbk_rltd/sequence/2014/03/30/00/,/sys/edw/ngdf_fdbk_rltd/snapshot/2014/03/29/00 output=/sys/edw//ngdf_fdbk_rltd/snapshot/2014/03/30/00 metadata=NGDF_FDBK_RLTD.xml");
		test.add("RunSteps$-0330-222033--1001402023-step_1_of_1");
	//	normalizer.jobNameNormalize(test);

		// normalizer.clean(test);
		// normalizer.isSQLJob(test.get(0));
		// normalizer.isSQLJob(test.get(1));
	//	System.out.println(normalizer.getResultJobName());
		// normalizer.jobNameNormalize();
	}

	
}