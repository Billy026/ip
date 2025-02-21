# Bob User Guide

![Ui png](https://github.com/user-attachments/assets/17bb39e5-fc8f-4bb0-b550-95c1b2625d15)

Bob, your friendly neighbourhood task manager, is a greenfield task management project!
He is designed to help you store and retrieve to do tasks, deadlines and events.

## Adding to do tasks

Creates a to do task. A to do task only has a task name, with no deadlines.
Format: todo TASK_NAME

Example: `todo read books`

```
Sure. I've added this task:
[ ] | T | read books
Now you have 1 task in the list.
```

## Adding deadline tasks

Creates a deadline task. A deadline task has a task name and one deadline.
Format: deadline TASK_NAME /by DUE_DATE [TIME]

Example: `deadline assignment /by 1/1/2025 10:30`

```
Sure. I've added this task:
[ ] | D | assignment | by: 01/01/2025 10:30
Now you have 2 tasks in the list.
```

## Adding event tasks

Creates an event task. An event task has a task name, a start date and an end date.
Format: event TASK_NAME /from START_DATE [TIME] /to END_DATE [TIME]

Example: `event meeting /from 1/1/2025 20:00 /to 1/1/2025 21:00`

```
Sure. I've added this task:
[ ] | E | meeting | from: 01/01/2025 20:00 | to: 01/01/2025 21:00
Now you have 3 tasks in the list.
```

## Deleting tasks

Deletes a task by its index.
Format: delete TASK_INDEX

Example: `delete 1`

```
Alright. I've removed this task:
[ ] | T | read books
Now you have 2 tasks in the list.
```

## Finding tasks

Lists down all tasks containing TASK_NAME.
Format: find TASK_NAME

Example: `find meeting`

```
Here are the matching tasks in your list:
1. [ ] | E | meeting | from: 01/01/2025 20:00 | to: 01/01/2025 21:00
```

## Getting tasks by their due date

Lists down all tasks with the specified due date.
If inputted due date does not have time, time will not be accounted for when getting matching tasks.
Format: getDueDate DUE_DATE [TIME]

Example: `getDueDate 01/01/2025`

```
Here's the tasks due at that date:
[ ] | D | assignment | by: 01/01/2025 10:30
[ ] | E | meeting | from: 01/01/2025 20:00 | to: 01/01/2025 21:00
```

## Listing all tasks

Lists all existing tasks.
Format: list

Example: `list`

```
Here are the tasks in your list:
1. [ ] | D | assignment | by: 01/01/2025 10:30
2. [ ] | E | meeting | from: 01/01/2025 20:00 | to: 01/01/2025 21:00
```

## Marking tasks

Marks a task at the given index as completed.
Format: mark TASK_INDEX

Example: `mark 1`

```
Nice! I've marked this task as done:
[X] | D | assignment | by: 01/01/2025 10:30
```

## Unmarking tasks

Marks a task at the given index as incomplete.
Format: unmark TASK_INDEX

Example: `unmark 1`

```
Oh, I guess it's not done yet:
[ ] | D | assignment | by: 01/01/2025 10:30
```

## Help

Lists down all the above commands.
Format: help
