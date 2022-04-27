package ir.caspco.versatile.application.card.wallet.hampa.context.services;

import ir.caspco.versatile.application.card.wallet.hampa.context.vo.InternetPackageResultVO;
import ir.caspco.versatile.application.card.wallet.hampa.context.vo.InternetPackageSearchEntranceVO;
import ir.caspco.versatile.jms.client.common.enums.thirdparty.ThirdPartyGroupType;
import ir.caspco.versatile.jms.client.common.vo.ThirdPartyConfigVO;
import ir.caspco.versatile.jms.client.common.vo.ThirdPartySubGroupVO;

import java.util.List;

/**
 * @author Davood Akbari - 1400
 * daak1365@gmail.com
 * daak1365@yahoo.com
 * 09125188694
 */

public interface ThirdPartyConfigService {

    List<ThirdPartyConfigVO> loadThirdPartyConfigsByParentId(Long parentId);

    List<ThirdPartySubGroupVO> loadByThirdPartyGroupType(ThirdPartyGroupType thirdPartyGroupType);

    List<InternetPackageResultVO> searchInternetPackages(InternetPackageSearchEntranceVO internetPackageSearch);
}
