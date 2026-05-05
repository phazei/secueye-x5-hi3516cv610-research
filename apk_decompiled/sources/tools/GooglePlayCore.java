package tools;

import android.app.Activity;
import androidx.annotation.NonNull;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.Task;

/* JADX INFO: loaded from: classes4.dex */
public class GooglePlayCore {

    public interface GooglePlayFlowListener {
        void OnCompleteListener();

        void OnErrorListener();
    }

    public static void launchGooglePlay(@NonNull final Activity activity2, final GooglePlayFlowListener googlePlayFlowListener) {
        final ReviewManager reviewManagerCreate = ReviewManagerFactory.create(activity2);
        reviewManagerCreate.requestReviewFlow().addOnCompleteListener(new OnCompleteListener() { // from class: tools.-$$Lambda$GooglePlayCore$fcHr3ZE3G_vW59q6fbDzfRqJJWk
            @Override // com.google.android.play.core.tasks.OnCompleteListener
            public final void onComplete(Task task) {
                GooglePlayCore.lambda$launchGooglePlay$1(reviewManagerCreate, activity2, googlePlayFlowListener, task);
            }
        });
    }

    static /* synthetic */ void lambda$launchGooglePlay$1(ReviewManager reviewManager, Activity activity2, final GooglePlayFlowListener googlePlayFlowListener, Task task) {
        if (task.isSuccessful()) {
            reviewManager.launchReviewFlow(activity2, (ReviewInfo) task.getResult()).addOnCompleteListener(new OnCompleteListener() { // from class: tools.-$$Lambda$GooglePlayCore$QLEzIfjGlrlGmGnr88b5LKqOm2M
                @Override // com.google.android.play.core.tasks.OnCompleteListener
                public final void onComplete(Task task2) {
                    GooglePlayCore.lambda$null$0(googlePlayFlowListener, task2);
                }
            });
        } else if (googlePlayFlowListener != null) {
            googlePlayFlowListener.OnErrorListener();
        }
    }

    static /* synthetic */ void lambda$null$0(GooglePlayFlowListener googlePlayFlowListener, Task task) {
        if (googlePlayFlowListener != null) {
            googlePlayFlowListener.OnCompleteListener();
        }
    }
}
