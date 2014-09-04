package de.alexanderkose.util;

/* package */class ReorderEntity implements Comparable<ReorderEntity> {
	private final String value;
	private String sort;

	/* package */ReorderEntity(String value, String sort) {
		this.value = value;
		this.sort = sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	@Override
	public int compareTo(ReorderEntity o) {
		return sort.compareTo(o.sort);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ReorderEntity other = (ReorderEntity) obj;
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return value;
	}
}