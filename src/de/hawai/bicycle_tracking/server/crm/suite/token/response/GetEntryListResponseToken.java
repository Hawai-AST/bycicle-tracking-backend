package de.hawai.bicycle_tracking.server.crm.suite.token.response;

import java.util.ArrayList;
import java.util.HashMap;

import de.hawai.bicycle_tracking.server.crm.suite.token.Token;
import de.hawai.bicycle_tracking.server.crm.suite.token.request.EntryListToken;


// {
//"result_count": 2,
//"total_count": "2",
//"next_offset": 2,
//"entry_list": [
//  {
//    "id": "8fc04b4c-6c35-2079-4daa-555e056af2cb",
//    "module_name": "AOS_Products",
//    "name_value_list": {
//      "id": {
//        "name": "id",
//        "value": "8fc04b4c-6c35-2079-4daa-555e056af2cb"
//      },
//      "name": {
//        "name": "name",
//        "value": "Cross Bike"
//      }
//    }
//  },
//  {
//    "id": "d3b44b8d-2e2a-be98-9db7-555de0e52688",
//    "module_name": "AOS_Products",
//    "name_value_list": {
//      "id": {
//        "name": "id",
//        "value": "d3b44b8d-2e2a-be98-9db7-555de0e52688"
//      },
//      "name": {
//        "name": "name",
//        "value": "City Bike"
//      }
//    }
//  }
//],
//"relationship_list": [
//  
//]
//}

public class GetEntryListResponseToken implements Token {
	
	private int result_count;
	private int total_count;
	private int next_offset;
	private ArrayList<EntryListToken> entry_list;
	private ArrayList<ArrayList<HashMap<String, Object>>> relationship_list;
	
	public GetEntryListResponseToken() {
		super();
	}
	
	public int getResult_count() {
		return result_count;
	}
	
	public void setResult_count(int result_count) {
		this.result_count = result_count;
	}
	
	public int getTotal_count() {
		return total_count;
	}
	
	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}
	
	public int getNext_offset() {
		return next_offset;
	}
	
	public void setNext_offset(int next_offset) {
		this.next_offset = next_offset;
	}
	
	public ArrayList<EntryListToken> getEntry_list() {
		return entry_list;
	}
	
	public void setEntry_list(ArrayList<EntryListToken> entry_list) {
		this.entry_list = entry_list;
	}
	
	public ArrayList<ArrayList<HashMap<String, Object>>> getRelationship_list() {
		return relationship_list;
	}
	
	public void setRelationship_list(ArrayList<ArrayList<HashMap<String, Object>>> relationship_list) {
		this.relationship_list = relationship_list;
	}

	@Override
	public String toString() {
		return "GetEntryListResponseToken [result_count=" + result_count + ", total_count=" + total_count + ", next_offset=" + next_offset
				+ ", entry_list=" + entry_list + ", relationship_list=" + relationship_list + "]";
	}

}
