
/**
 * DMSMessageReceiverInOut.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.1  Built on : Oct 19, 2009 (10:59:00 EDT)
 */
        package org.simplegrid.app.dms;

        /**
        *  DMSMessageReceiverInOut message receiver
        */

        public class DMSMessageReceiverInOut extends org.apache.axis2.receivers.AbstractInOutMessageReceiver{


        public void invokeBusinessLogic(org.apache.axis2.context.MessageContext msgContext, org.apache.axis2.context.MessageContext newMsgContext)
        throws org.apache.axis2.AxisFault{

        try {

        // get the implementation class for the Web Service
        Object obj = getTheImplementationObject(msgContext);

        DMSSkeletonInterface skel = (DMSSkeletonInterface)obj;
        //Out Envelop
        org.apache.axiom.soap.SOAPEnvelope envelope = null;
        //Find the axisOperation that has been set by the Dispatch phase.
        org.apache.axis2.description.AxisOperation op = msgContext.getOperationContext().getAxisOperation();
        if (op == null) {
        throw new org.apache.axis2.AxisFault("Operation is not located, if this is doclit style the SOAP-ACTION should specified via the SOAP Action to use the RawXMLProvider");
        }

        java.lang.String methodName;
        if((op.getName() != null) && ((methodName = org.apache.axis2.util.JavaUtils.xmlNameToJavaIdentifier(op.getName().getLocalPart())) != null)){

        

            if("submit".equals(methodName)){
                
                org.simplegrid.app.dms.SubmitResponse submitResponse9 = null;
	                        org.simplegrid.app.dms.Submit wrappedParam =
                                                             (org.simplegrid.app.dms.Submit)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    org.simplegrid.app.dms.Submit.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               submitResponse9 =
                                                   
                                                   
                                                         skel.submit(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), submitResponse9, false);
                                    } else 

            if("getStatus".equals(methodName)){
                
                org.simplegrid.app.dms.GetStatusResponse getStatusResponse11 = null;
	                        org.simplegrid.app.dms.GetStatus wrappedParam =
                                                             (org.simplegrid.app.dms.GetStatus)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    org.simplegrid.app.dms.GetStatus.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getStatusResponse11 =
                                                   
                                                   
                                                         skel.getStatus(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getStatusResponse11, false);
                                    } else 

            if("visualize".equals(methodName)){
                
                org.simplegrid.app.dms.VisualizeResponse visualizeResponse13 = null;
	                        org.simplegrid.app.dms.Visualize wrappedParam =
                                                             (org.simplegrid.app.dms.Visualize)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    org.simplegrid.app.dms.Visualize.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               visualizeResponse13 =
                                                   
                                                   
                                                         skel.visualize(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), visualizeResponse13, false);
                                    } else 

            if("getResult".equals(methodName)){
                
                org.simplegrid.app.dms.GetResultResponse getResultResponse15 = null;
	                        org.simplegrid.app.dms.GetResult wrappedParam =
                                                             (org.simplegrid.app.dms.GetResult)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    org.simplegrid.app.dms.GetResult.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getResultResponse15 =
                                                   
                                                   
                                                         skel.getResult(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getResultResponse15, false);
                                    
            } else {
              throw new java.lang.RuntimeException("method not found");
            }
        

        newMsgContext.setEnvelope(envelope);
        }
        }
        catch (java.lang.Exception e) {
        throw org.apache.axis2.AxisFault.makeFault(e);
        }
        }
        
        //
            private  org.apache.axiom.om.OMElement  toOM(org.simplegrid.app.dms.Submit param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(org.simplegrid.app.dms.Submit.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(org.simplegrid.app.dms.SubmitResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(org.simplegrid.app.dms.SubmitResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(org.simplegrid.app.dms.GetStatus param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(org.simplegrid.app.dms.GetStatus.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(org.simplegrid.app.dms.GetStatusResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(org.simplegrid.app.dms.GetStatusResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(org.simplegrid.app.dms.Visualize param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(org.simplegrid.app.dms.Visualize.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(org.simplegrid.app.dms.VisualizeResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(org.simplegrid.app.dms.VisualizeResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(org.simplegrid.app.dms.GetResult param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(org.simplegrid.app.dms.GetResult.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(org.simplegrid.app.dms.GetResultResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(org.simplegrid.app.dms.GetResultResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, org.simplegrid.app.dms.SubmitResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(org.simplegrid.app.dms.SubmitResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private org.simplegrid.app.dms.SubmitResponse wrapsubmit(){
                                org.simplegrid.app.dms.SubmitResponse wrappedElement = new org.simplegrid.app.dms.SubmitResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, org.simplegrid.app.dms.GetStatusResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(org.simplegrid.app.dms.GetStatusResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private org.simplegrid.app.dms.GetStatusResponse wrapgetStatus(){
                                org.simplegrid.app.dms.GetStatusResponse wrappedElement = new org.simplegrid.app.dms.GetStatusResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, org.simplegrid.app.dms.VisualizeResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(org.simplegrid.app.dms.VisualizeResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private org.simplegrid.app.dms.VisualizeResponse wrapvisualize(){
                                org.simplegrid.app.dms.VisualizeResponse wrappedElement = new org.simplegrid.app.dms.VisualizeResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, org.simplegrid.app.dms.GetResultResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(org.simplegrid.app.dms.GetResultResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private org.simplegrid.app.dms.GetResultResponse wrapgetResult(){
                                org.simplegrid.app.dms.GetResultResponse wrappedElement = new org.simplegrid.app.dms.GetResultResponse();
                                return wrappedElement;
                         }
                    


        /**
        *  get the default envelope
        */
        private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory){
        return factory.getDefaultEnvelope();
        }


        private  java.lang.Object fromOM(
        org.apache.axiom.om.OMElement param,
        java.lang.Class type,
        java.util.Map extraNamespaces) throws org.apache.axis2.AxisFault{

        try {
        
                if (org.simplegrid.app.dms.Submit.class.equals(type)){
                
                           return org.simplegrid.app.dms.Submit.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (org.simplegrid.app.dms.SubmitResponse.class.equals(type)){
                
                           return org.simplegrid.app.dms.SubmitResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (org.simplegrid.app.dms.GetStatus.class.equals(type)){
                
                           return org.simplegrid.app.dms.GetStatus.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (org.simplegrid.app.dms.GetStatusResponse.class.equals(type)){
                
                           return org.simplegrid.app.dms.GetStatusResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (org.simplegrid.app.dms.Visualize.class.equals(type)){
                
                           return org.simplegrid.app.dms.Visualize.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (org.simplegrid.app.dms.VisualizeResponse.class.equals(type)){
                
                           return org.simplegrid.app.dms.VisualizeResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (org.simplegrid.app.dms.GetResult.class.equals(type)){
                
                           return org.simplegrid.app.dms.GetResult.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (org.simplegrid.app.dms.GetResultResponse.class.equals(type)){
                
                           return org.simplegrid.app.dms.GetResultResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
        } catch (java.lang.Exception e) {
        throw org.apache.axis2.AxisFault.makeFault(e);
        }
           return null;
        }



    

        /**
        *  A utility method that copies the namepaces from the SOAPEnvelope
        */
        private java.util.Map getEnvelopeNamespaces(org.apache.axiom.soap.SOAPEnvelope env){
        java.util.Map returnMap = new java.util.HashMap();
        java.util.Iterator namespaceIterator = env.getAllDeclaredNamespaces();
        while (namespaceIterator.hasNext()) {
        org.apache.axiom.om.OMNamespace ns = (org.apache.axiom.om.OMNamespace) namespaceIterator.next();
        returnMap.put(ns.getPrefix(),ns.getNamespaceURI());
        }
        return returnMap;
        }

        private org.apache.axis2.AxisFault createAxisFault(java.lang.Exception e) {
        org.apache.axis2.AxisFault f;
        Throwable cause = e.getCause();
        if (cause != null) {
            f = new org.apache.axis2.AxisFault(e.getMessage(), cause);
        } else {
            f = new org.apache.axis2.AxisFault(e.getMessage());
        }

        return f;
    }

        }//end of class
    