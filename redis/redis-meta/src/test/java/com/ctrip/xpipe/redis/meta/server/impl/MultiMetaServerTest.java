package com.ctrip.xpipe.redis.meta.server.impl;

import java.util.LinkedList;
import java.util.List;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Test;

import com.ctrip.xpipe.redis.meta.server.AbstractMetaServerTest;
import com.ctrip.xpipe.redis.meta.server.MetaServer;
import com.ctrip.xpipe.redis.meta.server.rest.ForwardInfo;

import static org.mockito.Mockito.*;

/**
 * @author wenchao.meng
 *
 * Nov 30, 2016
 */
public class MultiMetaServerTest extends AbstractMetaServerTest{
	
	@Test
	public void testMultiProxy(){
		
		int serversCount = 10;
		List<MetaServer> servers = new LinkedList<>();
		
		for(int i=0; i < serversCount ; i++){
			
			servers.add(mock(MetaServer.class));
		}
		
		MetaServer metaServer = MultiMetaServer.newProxy(servers);
		
		final ForwardInfo forwardInfo = new ForwardInfo();
		
		metaServer.clusterDeleted(getClusterId(), forwardInfo);
		
		for(MetaServer mockServer :  servers){
			verify(mockServer).clusterDeleted(eq(getClusterId()),  argThat(new BaseMatcher<ForwardInfo>() {

				@Override
				public boolean matches(Object item) {
					//should be cloned
					if(item == forwardInfo){
						return false;
					}
					return true;
				}
				@Override
				public void describeTo(Description description) {
					
				}
			}));
		}
		
	}

}
