package com.vernon.webspider.school.extractor.renren;

import org.apache.log4j.Logger;
import org.htmlparser.NodeFilter;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;

import com.vernon.webspider.core.Extractor;
import com.vernon.webspider.core.http.Charset;

/**
 *
 * @author Vernon.Chen
 * @version 1.0 2013-4-12
 */
public class RenrenSchoolExtractor
		extends Extractor {

	private static final Logger LOGGER = Logger.getLogger(RenrenSchoolExtractor.class);

	/**
	 * constructor method
	 * 
	 * @param encoding
	 */
	public RenrenSchoolExtractor(String encoding) {
		super(encoding);
	}

	@Override
	public Object extract() throws Exception {
		LOGGER.debug("************************** RENREN website spider start **************************");
		NodeFilter filter = new AndFilter(new TagNameFilter("ul"), new HasAttributeFilter("id", "popup-country"));
		NodeList nodes = this.getParser().parse(filter);
		if (null == nodes || nodes.size() > 1) {
			LOGGER.info("提取最近更新ul失败");
			return null;
		}
		return null;
	}

	//------------------------------------  OTHER METHODS ------------------------------------

	public static void main(String[] args) throws Exception {
		String cookies = "anonymid=hdh2g6q3a918l5; _r01_=1; __utma=151146938.1293754952.1363232379.1364963702.1365726753.3; __utmz=151146938.1365726753.3.3.utmcsr=photo.renren.com|utmccn=(referral)|utmcmd=referral|utmcct=/getalbumprofile.do; depovince=GUZ; jebecookies=59c3caad-4f5e-47e3-8bf3-a15d20d0cc20|||||; ick_login=807f3578-4bde-492b-a7df-a11fcb1a02b0; loginfrom=null; XNESSESSIONID=58f35fe9eb3b; vip=1; _de=DD09E06DEBC5E32BC5BC13FF67F7809D696BF75400CE19CC; p=9e1bb1c308afeaee346c0c938e1d95636; ap=234505346; first_login_flag=1; t=9d20b3d42f03cbc59eb5f7f5b19552306; societyguester=9d20b3d42f03cbc59eb5f7f5b19552306; id=234505346; xnsid=2be7bcb";
		Extractor extractor = new RenrenSchoolExtractor(Charset.UTF8.getValue());
		extractor.loadPageNotProxy("http://www.renren.com/234505346/profile?v=info&act=edit", cookies, Charset.UTF8);
		extractor.extract();
	}
}
