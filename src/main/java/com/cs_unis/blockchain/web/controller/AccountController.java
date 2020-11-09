package com.hades_region.blockchain.web.controller;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.hades_region.blockchain.account.Account;
import com.hades_region.blockchain.account.Personal;
import com.hades_region.blockchain.crypto.ECKeyPair;
import com.hades_region.blockchain.crypto.Keys;
import com.hades_region.blockchain.db.DBAccess;
import com.hades_region.blockchain.utils.JsonVo;
import com.hades_region.blockchain.web.vo.AccountVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author Robert Gerard
 * @since 18-4-8
 */
@RestController
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private Personal personal;
	@Autowired
	private DBAccess dbAccess;

	/**
	 * @param request
	 * @return
	 */
	@GetMapping("/new")
	public JsonVo newAccount(HttpServletRequest request) throws Exception {

		ECKeyPair keyPair = Keys.createEcKeyPair();
		Account account = personal.newAccount(keyPair);
		AccountVo vo = new AccountVo();
		BeanUtils.copyProperties(account, vo);
		vo.setPrivateKey(keyPair.exportPrivateKey());
		return new JsonVo(JsonVo.CODE_SUCCESS, "New account created, please remember your Address and Private Key.",
				vo);
	}

	/**
	 * @param request
	 * @return
	 */
	@GetMapping("/coinbase/get")
	public JsonVo coinbase(HttpServletRequest request) {

		Optional<Account> coinBaseAccount = dbAccess.getCoinBaseAccount();
		JsonVo success = JsonVo.success();
		if (coinBaseAccount.isPresent()) {
			success.setItem(coinBaseAccount.get());
		} else {
			success.setMessage("CoinBase Account is not created");
		}
		return success;
	}

	/**
	 * @return
	 */
	@PostMapping("/coinbase/set")
	public JsonVo setCoinbase(@RequestBody Map<String, String> params) {

		Preconditions.checkNotNull(params.get("address"), "address can not be null");
		dbAccess.putCoinBaseAddress(params.get("address"));
		return JsonVo.success();
	}

	/**
	 * @param request
	 * @return
	 */
	@GetMapping("/list")
	public JsonVo listAccounts(HttpServletRequest request) {

		List<Account> accounts = dbAccess.listAccounts();
		JsonVo success = JsonVo.success();
		success.setItem(accounts);
		return success;
	}
}
