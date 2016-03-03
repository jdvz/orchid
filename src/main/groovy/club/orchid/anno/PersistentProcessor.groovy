package club.orchid.anno

import org.codehaus.groovy.ast.AnnotatedNode
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.AnnotationCollectorTransform

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/2/16 11:35 AM
 */
class PersistentProcessor extends AnnotationCollectorTransform {
    @Override
    List<AnnotationNode> visit(AnnotationNode collector, AnnotationNode aliasAnnotationUsage, AnnotatedNode aliasAnnotated, SourceUnit source) {
        def attributes = aliasAnnotationUsage.getMembers()

        return super.visit(collector, aliasAnnotationUsage, aliasAnnotated, source)
    }
}
