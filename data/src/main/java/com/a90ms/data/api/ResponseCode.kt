package com.a90ms.data.api

object ResponseCode {
    /**
     *API 요청 URL의 프로토콜, 파라미터 등에 오류가 있는지 확인합니다.
     * */
    const val ERROR_SE01 = "SE01"

    /**
     * display 파라미터의 값이 허용 범위의 값(1~100)인지 확인합니다.
     * */
    const val ERROR_SE02 = "SE02"

    /**
     * start 파라미터의 값이 허용 범위의 값(1~1000)인지 확인합니다.
     * */
    const val ERROR_SE03 = "SE03"

    /**
     * sort 파라미터의 값에 오타가 있는지 확인합니다.
     * */
    const val ERROR_SE04 = "SE04"

    /**
     * 검색어를 UTF-8로 인코딩합니다.
     * */
    const val ERROR_SE05 = "SE05"

    /**
     * API 요청 URL에 오타가 있는지 확인합니다.
     * */
    const val ERROR_SE06 = "SE06"

    /**
     * 서버 내부에 오류가 발생했습니다. "개발자 포럼"에 오류를 신고해 주십시오.
     * */
    const val ERROR_SE99 = "SE99"

    const val ERROR_429 = "012"
}
