package logic;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import util.Task;

public class Sort {

	private Vector<Task> TaskList;

	public Sort(Vector<Task> TaskList) {
		this.TaskList = TaskList;
	}

	public void sortList() {
		if (!TaskList.isEmpty()) {
			checkDue();

			listComparator ls = new listComparator();

			Collections.sort(TaskList, ls);

			for (int i = 0; i < TaskList.size(); i++) {
				TaskList.get(i).setIndex(i + 1);
			}
		}
	}

	private void checkDue() {
		for (Task t : TaskList) {
			if (t.getEndTime() != null) {
				if (t.getEndTime().isBefore(LocalDateTime.now())) {
					t.markTaskAsDue();
				}else{
					t.markTaskAsUndue();
				}
			}
		}

	}

	class listComparator implements Comparator<Task> {

		// Sort task according to whether it is marked as done, endtime and
		// alphatical order
		@Override
		public int compare(Task a, Task b) {

			if (a.isDone() && b.isDone()) {
				return a.getTaskDesc().compareToIgnoreCase(b.getTaskDesc());
			} else if (a.isDone()) {
				return 1;
			} else if (b.isDone()) {
				return -1;
			} else {
				return compareType(a, b);
			}

		}

		private int compareType(Task a, Task b) {

			Task.TASK_TYPE typeA = a.getTaskType();
			Task.TASK_TYPE typeB = b.getTaskType();
			if (typeA.equals(Task.TASK_TYPE.FLOATING_TASK)
					&& typeB.equals(Task.TASK_TYPE.FLOATING_TASK)) {
				return a.getTaskDesc().compareToIgnoreCase(b.getTaskDesc());
			} else if (typeA.equals(Task.TASK_TYPE.FLOATING_TASK)) {
				return 1;
			} else if (typeB.equals(Task.TASK_TYPE.FLOATING_TASK)) {
				return -1;
			} else {
				if (a.getEndTime().isBefore(b.getEndTime())) {
					return -1;
				} else {
					return 1;
				}
			}
		}

	}
}
