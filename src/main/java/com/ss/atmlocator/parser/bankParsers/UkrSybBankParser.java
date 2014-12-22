package com.ss.atmlocator.parser.bankParsers;

import com.ss.atmlocator.entity.AtmOffice;
import com.ss.atmlocator.parser.IParser;
import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

import static com.ss.atmlocator.entity.AtmOffice.AtmType.IS_ATM;
import static com.ss.atmlocator.entity.AtmOffice.AtmType.IS_ATM_OFFICE;
import static com.ss.atmlocator.entity.AtmOffice.AtmType.IS_OFFICE;

/**
 * Created by Roman Vintonyak on 05.11.2014.
 */
public class UkrSybBankParser implements IParser {
    private final Logger logger = Logger.getLogger(UkrSybBankParser.class);
    public static final String TOTAL_BRANCHES_SELECTOR = "#ref_goggle_tab_branches span";
    public static final String TOTAL_ATMS_SELECTOR = "#ref_goggle_tab_atms span";
    public static final String BRANCHES_SELECTOR = "#goggle_tab_branches .address";
    public static final String ATMS_SELECTOR = "#goggle_tab_atms .address";
    public static final String URL = "http://my.ukrsibbank.com/ua/branches_atms/map/index.php";
    public static final int RECORDS_PER_PAGE = 100;
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko";
    private Map<String, String> initSettings = new HashMap<>();

    private Map<String, String> initSettings(String page) {
        initSettings.put("region_id_13", "0");
        initSettings.put("type_branch_13", "1");
        initSettings.put("tab_id", "goggle_tab_branches");
        initSettings.put("page_b_13", page);
        initSettings.put("type_atm_13", "1");
        initSettings.put("type_atm_id_13[0]", "7");
        initSettings.put("type_atm_id_13[1]", "8");
        initSettings.put("page_a_13", page);
        initSettings.put("tab_id", "goggle_tab_atms");
        return initSettings;
    }

    @Override
    public void setParameter(Map<String, String> parameters) {

    }

    @Override
    public List<AtmOffice> parse() throws IOException {
        List<AtmOffice> resultListAtms = new ArrayList<>();
        int totalAtms = getElementsCount(TOTAL_ATMS_SELECTOR);
        int totalBranches = getElementsCount(TOTAL_BRANCHES_SELECTOR);
        int totalAtmPages = getTotalPages(totalAtms);
        int totalBranchPages = getTotalPages(totalBranches);

        List<AtmOffice> branches = getListElements(totalBranchPages, BRANCHES_SELECTOR);
        List<AtmOffice> atms = getListElements(totalAtmPages, ATMS_SELECTOR);
        AtmOffice atmOffice = null;
        for (AtmOffice branch : branches) {
            atmOffice = branch;
            if (atms.contains(branch)) {
                atmOffice.setType(IS_ATM_OFFICE);
                atms.remove(branch);
                logger.debug("Found branch and ATM at same address: "+branch.getAddress());
            } else {
                atmOffice.setType(IS_OFFICE);
            }
            resultListAtms.add(atmOffice);
        }
        logger.debug("Total branches added: " + resultListAtms.size());
        resultListAtms.addAll(atms);
        return resultListAtms;
    }

    private List<AtmOffice> getListElements(int totalPages, String selector) {
        Document document = null;
        List<AtmOffice> listElements = new ArrayList<>();
        AtmOffice.AtmType atmType = selector.equals(ATMS_SELECTOR) ? IS_ATM : IS_OFFICE;
        for (int page = 1; page <= totalPages; page++) {
            try {
                document = getDocument(initSettings(String.valueOf(page)));
            } catch (IOException ioe) {
                logger.error(ioe.getMessage(),ioe);
                return null;
            }
            Elements elements = document.select(selector);
            for (Element element : elements) {
                listElements.add(new AtmOffice(element.text(), atmType));
            }
        }
        return listElements;
    }

    private int getTotalPages(int countElements) throws IOException {

        return (int) Math.ceil(countElements / (double)RECORDS_PER_PAGE);
    }

    private int getElementsCount(String selector) throws IOException {
        Document countAtms = getDocument(initSettings("1"));
        String textCount = countAtms.select(selector).text();
        int count = Integer.parseInt(textCount.replaceAll("\\D+", ""));
        return count;
    }


    private Document getDocument(Map<String, String> params) throws IOException {
        return Jsoup.connect(URL)
                .data(params)
                .header("Host", "my.ukrsibbank.com")
                .referrer("http://my.ukrsibbank.com/ua/branches_atms/map/index.php")
                .userAgent(USER_AGENT)
                .timeout(5 * 1000)
                .get();
    }

    public static void main(String[] args) {
        UkrSybBankParser ukrSybBankParser = new UkrSybBankParser();
        try {
            System.out.println(ukrSybBankParser.parse());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
