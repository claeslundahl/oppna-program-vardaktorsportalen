package se.vgregion.portal.vap.util.project;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.model.AssetTag;
import com.liferay.portlet.asset.service.AssetEntryServiceUtil;
import com.liferay.portlet.asset.service.AssetTagLocalServiceUtil;
import com.liferay.portlet.asset.service.persistence.AssetEntryQuery;
import com.liferay.portlet.journal.NoSuchArticleException;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalArticleResource;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalArticleResourceLocalServiceUtil;

/**
 * @author Erik Andersson
 */

public class ProjectUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectUtil.class);
	
	public static List<JournalArticle> getAllProjectArticles(long groupId) throws PortalException, SystemException {

		int start = 0;
		int end = getProjectAssetEntryCount(groupId);
		
    	List<JournalArticle> articles = getProjectArticles(groupId, start, end);
    	
    	return articles;
    }
	
	public static JournalArticle getLatestProjectArticle(long groupId) throws PortalException, SystemException {
		
		System.out.println("ProjectUtil - getLatestProjectArticle");
		
		JournalArticle article = null;
		
		int start = 0;
		int end = 1;
		
		List<JournalArticle> articles = getProjectArticles(groupId, start, end);
		
		if(articles.size() > 0 ) {
			article = articles.get(0);
		}
		
		return article;
	}
	
	public static List<JournalArticle> getProjectArticles(long groupId, int start, int end) throws PortalException, SystemException {
		ArrayList<JournalArticle> articles = new ArrayList<JournalArticle>();
		
		List<AssetEntry> assetEntries = getProjectAssetEntries(groupId, start, end);
		
    	for(AssetEntry assetEntry : assetEntries) {
    		
    		long classPK = assetEntry.getClassPK();
    		
    		try {
    			
    			JournalArticleResource journalArticleResource = JournalArticleResourceLocalServiceUtil.getJournalArticleResource(classPK);
    			
    			String articleId = journalArticleResource.getArticleId();
    			
    			JournalArticle journalArticle = JournalArticleLocalServiceUtil.getArticle(groupId, articleId);
    			
    			articles.add(journalArticle);
    		} catch(NoSuchArticleException e) {}
    	}
		
		return articles;
	}
	
	public static JournalArticle getProjectArticleByUrlTitle(long groupId, String urlTitle) {
		JournalArticle article = null;
		
		try {
			article = JournalArticleLocalServiceUtil.getArticleByUrlTitle(groupId, urlTitle);	
		} catch(Exception e) {
			if(e instanceof NoSuchArticleException) {
				System.out.println("ProjectUtil - getProjectArticleByUrlTitle - the requested article was not found.");
			}
		}
		
		
		
		return article;
	}
    
	private static List<AssetEntry> getProjectAssetEntries(long groupId, int start, int end)  throws PortalException, SystemException  {
    	
    	List<AssetEntry> assetEntries = new ArrayList<AssetEntry>();
    	
		AssetTag assetTag = AssetTagLocalServiceUtil.getTag(groupId, ProjectConstants.PROJECT_ASSET_TAG_NAME);
    		
    	if(assetTag != null) {
        	AssetEntryQuery assetEntryQuery = getProjectAssetEntryQueryBase(groupId);
        	
    		boolean orderByType1Asc = false;
    		
    		assetEntryQuery.setOrderByCol1("createDate");
    		assetEntryQuery.setOrderByType1(orderByType1Asc ? "ASC" : "DESC");
    		
    		assetEntryQuery.setStart(start);
    		assetEntryQuery.setEnd(end);
    		
    		assetEntries = AssetEntryServiceUtil.getEntries(assetEntryQuery);
    	}
    	
    	return assetEntries;
    }
	
	private static int getProjectAssetEntryCount(long groupId) throws PortalException, SystemException {

    	int entriesCount = 0;
    	
		AssetEntryQuery assetEntryQuery = getProjectAssetEntryQueryBase(groupId);
		
		entriesCount = AssetEntryServiceUtil.getEntriesCount(assetEntryQuery);
    	
    	return entriesCount;
		
		
	}

	private static AssetEntryQuery getProjectAssetEntryQueryBase(long groupId) throws PortalException, SystemException {
		AssetEntryQuery assetEntryQuery = new AssetEntryQuery();
		
    	long[] groupIds = new long []{groupId};
    	
    	AssetTag assetTag = AssetTagLocalServiceUtil.getTag(groupId, ProjectConstants.PROJECT_ASSET_TAG_NAME);
    	
		long assetTagId = assetTag.getTagId();
		long[] tagsIds = new long []{assetTagId};
		
    	assetEntryQuery.setClassName(JournalArticle.class.getName());
    	assetEntryQuery.setGroupIds(groupIds);
    	assetEntryQuery.setExcludeZeroViewCount(false);
		assetEntryQuery.setAnyTagIds(tagsIds);
		
		return assetEntryQuery;
	}
	
}
