/**
 * Contains mixins defining methods that overlap with DOM. All methods defined here have the same behavior in
 * DOM and Axiom. In order to avoid conflicts during weaving, this aspect will be removed
 * dynamically if the DOM and Axiom frontends are used simultaneously. Thus, the methods defined by
 * the DOM frontend will take precedence over the methods defined here.
 * 
 * @author Andreas Veithen
 */
package com.google.code.ddom.frontend.axiom.mixin.dom;