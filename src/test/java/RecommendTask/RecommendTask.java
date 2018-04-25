package RecommendTask;

import java.util.List;

import vos.RequesterTaskInfo;
import vos.WorkerInfo;

import java.sql.Timestamp;

public interface RecommendTask {
    @SuppressWarnings("rawtypes")
    String[] getRecommendTask(int worker_number, int times, List<WorkerInfo> workerInfos, RequesterTaskInfo requesterInfos);

    Timestamp getTakenDeadline(int worker_number, int times, int mins , List<WorkerInfo> workerInfos, RequesterTaskInfo requesterInfos);
}
