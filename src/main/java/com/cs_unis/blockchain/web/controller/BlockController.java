package com.hades_region.blockchain.web.controller;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.hades_region.blockchain.core.Block;
import com.hades_region.blockchain.core.BlockChain;
import com.hades_region.blockchain.core.Transaction;
import com.hades_region.blockchain.crypto.Credentials;
import com.hades_region.blockchain.db.DBAccess;
import com.hades_region.blockchain.net.base.Node;
import com.hades_region.blockchain.utils.JsonVo;
import com.hades_region.blockchain.web.vo.TransactionVo;
import com.hades_region.blockchain.conf.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author Robert Gerard
 * @since 2018-04-07 上午10:50.
 */
@RestController
@RequestMapping("/chain")
public class BlockController {

	@Autowired
	private DBAccess dbAccess;
	@Autowired
	private BlockChain blockChain;
	@Autowired
	private Settings settings;


	@GetMapping({"", "/", "index"})
	public JsonVo index(HttpServletRequest request) {
		return JsonVo.success();
	}

	/**
	 * @param request
	 * @return
	 */
	@GetMapping("/mine")
	public JsonVo mine(HttpServletRequest request) throws Exception {

		Block block = blockChain.mining();
		JsonVo vo = new JsonVo();
		vo.setCode(JsonVo.CODE_SUCCESS);
		vo.setMessage("Create a new block");
		vo.setItem(block);
		return vo;
	}

	/**
	 * @param request
	 * @return
	 */
	@GetMapping("/block/view")
	public JsonVo viewChain(HttpServletRequest request) {

		Optional<Block> block = dbAccess.getLastBlock();
		JsonVo success = JsonVo.success();
		if (block.isPresent()) {
			success.setItem(block.get());
		}
		return success;

	}

	/**
	 * @param txVo
	 * @return
	 */
	@PostMapping("/transactions/new")
	public JsonVo sendTransaction(@RequestBody TransactionVo txVo) throws Exception {
		Preconditions.checkNotNull(txVo.getTo(), "Recipient is needed.");
		Preconditions.checkNotNull(txVo.getAmount(), "Amount is needed.");
		Preconditions.checkNotNull(txVo.getPrivateKey(), "Private Key is needed.");
		Credentials credentials = Credentials.crdeate(txVo.getPrivateKey());
		Transaction transaction = blockChain.sendTransaction(
				credentials,
				txVo.getTo(),
				txVo.getAmount(),
				txVo.getData());

		if (settings.isAutoMining()) {
			blockChain.mining();
		}
		JsonVo success = JsonVo.success();
		success.setItem(transaction);
		return success;
	}

	/**
	 * @param node
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/node/add")
	public JsonVo addNode(@RequestBody Map<String, Object> node) throws Exception {

		Preconditions.checkNotNull(node.get("ip"), "server ip is needed.");
		Preconditions.checkNotNull(node.get("port"), "server port is need.");

		blockChain.addNode(String.valueOf(node.get("ip")), (Integer) node.get("port"));
		return JsonVo.success();
	}

	/**
	 * @param request
	 * @return
	 */
	@GetMapping("node/view")
	public JsonVo nodeList(HttpServletRequest request) {

		Optional<List<Node>> nodeList = dbAccess.getNodeList();
		JsonVo success = JsonVo.success();
		if (nodeList.isPresent()) {
			success.setItem(nodeList.get());
		}
		return success;
	}

}
