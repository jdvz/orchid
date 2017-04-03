package club.orchid.domain.product

import club.orchid.domain.BlossomStatus

/**
 * @author Dmitri Zaporozhtsev <dmitri.zera@gmail.com>
 */
public interface IProduct {
    long getId()
    int getVersion()
    String getDescription()
    int getShootCount()
    BlossomStatus getBlossomStatus()
    boolean isLeaf()
    boolean isEnabled()
}