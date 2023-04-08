package jt.projects.repository.retrofit

interface DataSource<T> {
    suspend fun getData():T
}