package org.tinos.ortho.fhmm.imp;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.tinos.engine.euclid.imp.EuclidControllerImp;
import org.tinos.ortho.fhmm.FMHMMList;
import org.tinos.view.obj.FMHMMNode;
import org.tinos.view.stable.StableData;
public class FMHMMListImp implements FMHMMList{
	private Map <String, String> words;
	private Map <String, FMHMMNode> linkedHashMap;
	@SuppressWarnings(StableData.RAW_TYPES)
	private Map <Integer, Map> linkedHashMapRoot;
	public Map<String, FMHMMNode> getLinkedHashMap(){
		return linkedHashMap;
	}

	public void setLinkedHashMap(Map<String, FMHMMNode> linkedHashMap){
		this.linkedHashMap = linkedHashMap;
	}

	@SuppressWarnings(StableData.RAW_TYPES)
	public Map<Integer, Map> getLinkedHashMapRoot(){
		return linkedHashMapRoot;
	}

	@SuppressWarnings(StableData.RAW_TYPES)
	public void setLinkedHashMapRoot(Map<Integer, Map> linkedHashMapRoot){
		this.linkedHashMapRoot = linkedHashMapRoot;
	}

	public void setWords(Map<String, String> words){
		this.words = words;
	}

	@SuppressWarnings(StableData.RAW_TYPES)
	public Map<Integer, Map> getRoot(){
		return this.linkedHashMapRoot;
	}
	
	public void index() throws IOException{
		words = new ConcurrentHashMap <>();
		linkedHashMap = new ConcurrentHashMap <>();
		linkedHashMapRoot = new ConcurrentHashMap <>();
		InputStream in = getClass().getResourceAsStream(StableData.WORDS_SOURSE_LINK);
		BufferedReader cReader = new BufferedReader(new InputStreamReader(in, StableData.UTF8_STRING));  
		String cInputString = null; 
		while ((cInputString = cReader.readLine()) != null){  
			if(!cInputString.replace(StableData.SPACE_STRING, StableData.EMPTY_STRING).equals(StableData
					.EMPTY_STRING)&&cInputString.split(StableData.SLASH_STRING).length > StableData.INT_ONE){
					words.put(cInputString.split(StableData.SLASH_STRING)[StableData.INT_ZERO], cInputString.
							split(StableData.SLASH_STRING)[StableData.INT_ONE]);	
					linkedHashMap = loopLoadForest(cInputString);		 
			}
		}
		cReader.close();
		linkedHashMapRoot = new EuclidControllerImp().mcogsEuclid(linkedHashMap);	
	}
	
	public Map<String, FMHMMNode> loopLoadForest(String cInputString){
		for(int i = StableData.INT_ZERO; i < cInputString.length(); i++){
			if(linkedHashMap.containsKey(StableData.EMPTY_STRING + cInputString.charAt(i))){
				FMHMMNode fHHMMNode = linkedHashMap.get(StableData.EMPTY_STRING + cInputString.charAt(i));
				linkedHashMap = doNeroPostCognitive(fHHMMNode,cInputString,i);
			}else{
				FMHMMNode fHHMMNode = new FMHMMNode();
				fHHMMNode.setVb(StableData.EMPTY_STRING + cInputString.charAt(i));
				if(i + StableData.INT_ONE < cInputString.length()){
					Map<String, Integer> next = new ConcurrentHashMap<>();
					next.put(StableData.EMPTY_STRING + cInputString.charAt(i + StableData.INT_ONE)
					, StableData.INT_ONE);
					fHHMMNode.setNext(next);
				}
				linkedHashMap.put(StableData.EMPTY_STRING + cInputString.charAt(i), fHHMMNode);
			}
		}
		return linkedHashMap;
	}

	public Map<String, FMHMMNode> doNeroPostCognitive(FMHMMNode fFHMMNode, String cInputString, int i){
		if(fFHMMNode.getNext() != null){
			if(i + StableData.INT_ONE < cInputString.length()){
				linkedHashMap=doCheckAndRunNeroPostFix(fFHMMNode,cInputString,i);
			}
		}else{
			ConcurrentHashMap<String, Integer>  concurrentHashMap = new  ConcurrentHashMap<> ();
			if(i + StableData.INT_ONE < cInputString.length()){
				concurrentHashMap.put(StableData.EMPTY_STRING + cInputString.charAt(i + StableData.INT_ONE), 
						StableData.INT_ONE);
			} 
			fFHMMNode.setNext(concurrentHashMap);
			linkedHashMap.put(StableData.EMPTY_STRING + cInputString.charAt(i), fFHMMNode);
		}
		return linkedHashMap;
	}

	public Map<String, FMHMMNode> doCheckAndRunNeroPostFix(FMHMMNode fFHMMNode, String cInputString, int i){
		if(!fFHMMNode.getNext().containsKey(StableData.EMPTY_STRING + cInputString.charAt(i 
				+ StableData.INT_ONE))){
			Map<String, Integer> map = fFHMMNode.getNext();
			map.put(StableData.EMPTY_STRING + cInputString.charAt(i + StableData.INT_ONE)
			, StableData.INT_ONE);
			fFHMMNode.setNext(map);
			linkedHashMap.put(StableData.EMPTY_STRING + cInputString.charAt(i), fFHMMNode);
		}
		return linkedHashMap;
	}

	public Map<String, String> getWords(){
		return this.words;
	}
}