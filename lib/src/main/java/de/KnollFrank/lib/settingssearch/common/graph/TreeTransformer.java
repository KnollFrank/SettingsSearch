package de.KnollFrank.lib.settingssearch.common.graph;

public interface TreeTransformer<NSrc, VSrc, NDst, VDst> {

    record ContextOfInnerNode<VSrc, NDst>(VSrc valueOfEdgeFromParentNodeToInnerNode,
                                          NDst transformedParentNode) {
    }

    NDst transformRootNode(NSrc rootNode);

    NDst transformInnerNode(NSrc innerNode, ContextOfInnerNode<VSrc, NDst> contextOfInnerNode);

    VDst transformEdgeValue(VSrc edgeValue, NDst transformedParentNode);
}
