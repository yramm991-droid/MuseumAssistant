package com.museumassistant.automation

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.view.accessibility.AccessibilityEvent

class MuseumAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() {}
    /** Coordinates are screen pixels. Call only after explicit user enablement and a verified decision. */
    fun tap(x: Float, y: Float, callback: (() -> Unit)? = null) {
        val path = Path().apply { moveTo(x, y) }
        val gesture = GestureDescription.Builder().addStroke(GestureDescription.StrokeDescription(path, 0, 70)).build()
        dispatchGesture(gesture, object : GestureResultCallback() { override fun onCompleted(g: GestureDescription?) { callback?.invoke() } }, null)
    }
}
