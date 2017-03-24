package vos;

import java.util.ArrayList;

public class RequesterTaskVos extends TaskVos {

		//已提交任务的工人答案
		private ArrayList<ArrayList<String>> receiveAnswers;
		
		//这里answer是经过决策过后获取的答案， String对应sqlTarger，可能是一维或者二维数组
		private ArrayList<String> finalAnswers;
		
		//这是任务的标识符，表示当前任务的状态：待发布，发布中，未完成（超时间未完成），已完成（获取到正确答案）
		//雇主：created, published, finished, paused(和created区别在于paused状态是由published之后转换来的), expired
		private String taskState;

		//通过计算得到的该任务的难度系数
		private double difficultDegree;
		
		//该任务需要的工人数量， 先系统预估， 后经过雇主确认修改
		private int workerNumber;

		//该任务总的预计花费
		private double sumCost;

		//任务到目前为止的花费
		private double hasCost;

		public ArrayList<ArrayList<String>> getReceiveAnswers() {
			return receiveAnswers;
		}

		public void setReceiveAnswers(ArrayList<ArrayList<String>> receiveAnswers) {
			this.receiveAnswers = receiveAnswers;
		}

		public ArrayList<String> getFinalAnswers() {
			return finalAnswers;
		}

		public void setFinalAnswers(ArrayList<String> finalAnswers) {
			this.finalAnswers = finalAnswers;
		}

		public String getTaskState() {
			return taskState;
		}

		public void setTaskState(String taskState) {
			this.taskState = taskState;
		}

		public double getDifficultDegree() {
			return difficultDegree;
		}

		public void setDifficultDegree(double difficultDegree) {
			this.difficultDegree = difficultDegree;
		}

		public int getWorkerNumber() {
			return workerNumber;
		}

		public void setWorkerNumber(int workerNumber) {
			this.workerNumber = workerNumber;
		}

		public double getSumCost() {
			return sumCost;
		}

		public void setSumCost(double sumCost) {
			this.sumCost = sumCost;
		}

		public double getHasCost() {
			return hasCost;
		}

		public void setHasCost(double hasCost) {
			this.hasCost = hasCost;
		}
		
		
}
