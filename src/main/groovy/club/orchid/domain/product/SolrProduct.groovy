package club.orchid.domain.product

import club.orchid.anno.mapping.Primitive
import club.orchid.domain.BlossomStatus
import groovy.transform.Canonical
import org.apache.solr.client.solrj.beans.Field
import org.springframework.data.annotation.Id
import org.springframework.data.solr.core.mapping.SolrDocument

/**
 * @author Dmitri Zaporozhtsev <dmitri.zera@gmail.com>
 */
@SolrDocument
@Canonical
public class SolrProduct extends Named<SolrProduct> implements IProduct {
    public SolrProduct() {
    }

    private SolrProduct(Builder builder) {
        this.id = builder.id
        this.version = builder.version
        this.name = builder.name
        this.description = builder.description
        this.shootCount = builder.shootCount
        this.blossomStatus = builder.blossomStatus
        this.leaf = builder.leaf
        this.enabled = builder.enabled
    }
    @Id
    @Field
    long id
    @Field
    int version

    @Field
    String name
    @Field
    String description
    @Field
    int shootCount
    @Field
    BlossomStatus blossomStatus
    @Field
    boolean leaf
    @Field
    boolean enabled

    public static final class Builder {
        private long id;
        private int version;
        private String name;
        private String description;
        private int shootCount;
        private BlossomStatus blossomStatus;
        private boolean leaf;
        private boolean enabled;

        private Builder() {}

        public SolrProduct build() {
            return new SolrProduct(this);
        }


        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder version(long version) {
            this.version = version;
            return this;
        }
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder shootCount(int shootCount) {
            this.shootCount = shootCount;
            return this;
        }

        public Builder blossomStatus(BlossomStatus blossomStatus) {
            this.blossomStatus = blossomStatus;
            return this;
        }

        public Builder leaf(boolean leaf) {
            this.leaf = leaf;
            return this;
        }

        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }
    }
}
