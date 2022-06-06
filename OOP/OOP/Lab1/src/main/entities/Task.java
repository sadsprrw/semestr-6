package main.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
	private long id;
	private String name;
	private long project_id;
	private Role qualification;
	private long workers_num;
		
}
