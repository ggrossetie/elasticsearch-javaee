package org.mogztter.elasticsearch.javaee;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;

import junit.framework.Assert;

/**
 * @author bloemgracht
 */
@RunWith(Arquillian.class)
public class ElasticSearchTest {

    @Deployment
    public static EnterpriseArchive createDeployment() {
        MavenDependencyResolver resolver = DependencyResolvers.use(MavenDependencyResolver.class).configureFrom("pom.xml").goOffline();
        return ShrinkWrap.create(EnterpriseArchive.class)
                .addAsModule(
                        ShrinkWrap.create(JavaArchive.class)
                                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                                .addPackages(true, "org.mogztter.elasticsearch.javaee")
                                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml"))
                .addAsLibraries(
                        resolver.artifact("org.elasticsearch:elasticsearch:0.19.10")
                                .resolveAsFiles());
    }


    @Test
    public void search_jean_marc() {
        Client client = new TransportClient()
                .addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
        try {
            SearchResponse response = searchBySupplierName(client, "jean marc");
            Assert.assertEquals(1L, response.getHits().getTotalHits());
            response = searchBySupplierName(client, "JeaN MarC");
            Assert.assertEquals(1L, response.getHits().getTotalHits());
            response = searchBySupplierName(client, "jean-marc");
            Assert.assertEquals(1L, response.getHits().getTotalHits());
            response = searchBySupplierName(client, "jean-Marc");
            Assert.assertEquals(1L, response.getHits().getTotalHits());
            response = searchBySupplierName(client, "jean");
            Assert.assertEquals(2L, response.getHits().getTotalHits());
            response = searchBySupplierName(client, "marc");
            Assert.assertEquals(1L, response.getHits().getTotalHits());
        } finally {
            client.close();
        }
    }

    private SearchResponse searchBySupplierName(Client client, String value) {
        return client.prepareSearch("financial")
                .setTypes("invoice")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(matchQuery("supplier.name", value).operator(MatchQueryBuilder.Operator.AND))
                .setFrom(0).setSize(60).setExplain(true)
                .execute()
                .actionGet();
    }
}
