package chronomuncher.patches;

import chronomuncher.ChronoMod;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;

import com.megacrit.cardcrawl.metrics.Metrics;
import com.megacrit.cardcrawl.screens.DeathScreen;

import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class MetricsPatch {

	// @SpirePatch(cls="com.megacrit.cardcrawl.metrics.Metrics", method="sendPost")

	// public static class sendPost {
	// 	public static ExprEditor Instrument() {
	// 		return new ExprEditor() {
	// 			public void edit(MethodCall m) throws CannotCompileException {
	// 				if (m.getMethodName().equals("sendPost")) {
	// 					m.replace("sendPost(\"http://www.chronometry.ca/metrics/\", fileName);");
	// 				}
	// 			}
	// 		};
	// 	}	
	// }

	// @SpirePatch(cls="com.megacrit.cardcrawl.screens.DeathScreen", method="shouldUploadMetricData")
	// public static class shouldUploadMetricData {
	// 	public static boolean Postfix() {
	// 		return true;
	// 	}
	// }

}