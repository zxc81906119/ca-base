package com.redhat.cleanbase.convert.json.util;

import com.redhat.cleanbase.common.type.TypeRef;
import com.redhat.cleanbase.common.utils.CastUtils;
import com.redhat.cleanbase.convert.json.constant.JsonConstants;
import lombok.NonNull;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public final class JsonUtil {

    private static final Pattern SPLIT_VALUE_PATTERN = getPattern();

    private JsonUtil() {
        throw new UnsupportedOperationException();
    }

    public static Integer getNextIndex(Queue<Integer> indexQueue, boolean needPoll) {
        if (CollectionUtils.isEmpty(indexQueue)) {
            return null;
        }
        return needPoll ?
                indexQueue.poll()
                : indexQueue.peek();
    }

    public static Integer getNextIndex(Queue<Integer> indexQueue) {
        return getNextIndex(indexQueue, true);
    }

    public static void setPathValueToMap(@NonNull Map<String, Object> oriMap, @NonNull String path, Object value, int[] indexes) {
        val indexQueue = covertIndexes(indexes);
        checkPathIsNotEmpty(path);
        checkPathStartIsNotSplit(path);
        checkPathEndIsNotDot(path);
        // '[]' or '[n]' or '.'
        val splitValueMatcher = SPLIT_VALUE_PATTERN.matcher(path);
        // 第一次是傳入 map , 之後會變動
        Object lastObj = oriMap;
        String lastSplit = null;
        Integer lastArrayIndex = null;
        int findSplitIndex = 0;
        while (splitValueMatcher.find(findSplitIndex)) {

            val split = splitValueMatcher.group(1);
            val thisArrayIndexString = splitValueMatcher.group(2);

            val isDot = JsonConstants.Split.DOT.equals(split);

            val thisArrayIndex = getThisArrayIndex(indexQueue, thisArrayIndexString, isDot);

            val splitStartIndex = splitValueMatcher.start();
            val splitEndIndex = splitValueMatcher.end();

            lastObj = findSplitIndex == splitStartIndex ?
                    processIndexEq(path, lastObj, lastSplit, isDot, splitEndIndex, value, lastArrayIndex, thisArrayIndex) :
                    processIndexNotEq(path, lastObj, lastSplit, findSplitIndex, isDot, splitStartIndex, splitEndIndex, value, thisArrayIndex);

            findSplitIndex = splitEndIndex;
            lastSplit = split;
            lastArrayIndex = thisArrayIndex;
        }
        // 剩餘 => [(n)]a 不合法 , .a 合法
        if (findSplitIndex != path.length()) {
            checkSplitIsDotOrNull(lastSplit);
            checkIsMap(lastObj);
            setMapValue(CastUtils.cast(lastObj), path.substring(findSplitIndex), value);
        }
    }

    private static Integer getThisArrayIndex(Queue<Integer> indexQueue, String thisArrayIndexString, boolean isDot) {
        return StringUtils.isNotEmpty(thisArrayIndexString) ?
                // [n]
                Integer.valueOf(thisArrayIndexString) :
                // [] or .
                (!isDot ?
                        // []
                        getNextIndex(indexQueue) :
                        // .
                        null
                );
    }

    private static Pattern getPattern() {
        val keyRegExp = "(" + JsonConstants.Split.DOT_REGEXP + "|" + JsonConstants.Split.QUART_REGEXP + ")";
        return Pattern.compile(keyRegExp);
    }

    private static Queue<Integer> covertIndexes(int[] indexes) {
        if (indexes == null || indexes.length == 0) {
            return null;
        }
        val queue = new LinkedList<Integer>();
        Arrays.stream(indexes)
                .forEach(queue::add);
        return queue;
    }

    private static void setMapValue(Map<String, Object> lastObj, String key, Object value) {
        lastObj.put(key, value);
    }

    private static Object processIndexNotEq(String path, Object lastObj, String lastSplitValue, int findIndex, boolean isDot, int startIndex, int endIndex, Object value, Integer thisArrayIndex) {
        checkSplitIsDotOrNull(lastSplitValue);
        checkIsMap(lastObj);
        return processMap(path, findIndex, isDot, startIndex, endIndex, value, thisArrayIndex, CastUtils.cast(lastObj));
    }

    private static Object processIndexEq(String path, Object lastObj, String lastSplitValue, boolean isDot, int endIndex, Object value, Integer lastArrayIndex, Integer thisArrayIndex) {
        // 合法 : [(n)]. & [(n)][(n)] 不合法: .[(n)] & ..
        checkSplitIsNotDot(lastSplitValue);
        checkIsList(lastObj);
        return listProcess(path, isDot, endIndex, value, lastArrayIndex, thisArrayIndex, CastUtils.cast(lastObj));
    }

    private static Object processMap(String path, int findIndex, boolean isDot, int startIndex, int endIndex, Object value, Integer thisArrayIndex, Map<String, Object> lastObj) {
        // .a[(n)] or .a. or a[(n)] or a.
        // lastObj= someMap
        // thisObj=a
        // isDot= .a. a. 而非 .a[(n)] a[(n)]
        // thisArrayIndex= null or (n)
        // lastSplitValue=. or null
        // 目的抓到 thisObj

        val thisObj = getThisObjByMap(path, findIndex, isDot, startIndex, lastObj);

        // 還未結束,下次迴圈繼續做
        if (path.length() != endIndex) {
            return thisObj;
        }
        // 剛好結束,再做一次收尾
        // 一定是[(n)][(n)]
        val list = CastUtils.cast(thisObj, new TypeRef<List<Object>>() {
        });
        setListValue(value, thisArrayIndex, list);
        return list;
    }

    private static Object getThisObjByMap(String path, int findIndex, boolean isDot, int startIndex, Map<String, Object> lastObj) {
        val thisKey = path.substring(findIndex, startIndex);
        Object thisObj = lastObj.get(thisKey);
        if (thisObj == null) {
            lastObj.put(thisKey, thisObj = generateObj(isDot));
        } else {
            checkType(isDot, thisObj);
        }
        return thisObj;
    }

    private static void checkSplitIsDotOrNull(String lastSplitValue) {
        if (lastSplitValue != null && lastSplitValue.matches(JsonConstants.Split.QUART_REGEXP)) {
            throw new RuntimeException();
        }
    }

    private static void checkPathStartIsNotSplit(String path) {
        if (path.startsWith(JsonConstants.Split.DOT) || path.startsWith(JsonConstants.Split.LEFT_QUART)) {
            throw new RuntimeException();
        }
    }


    private static Object listProcess(String path, boolean isDot, int endIndex, Object value, Integer lastArrayIndex, Integer thisArrayIndex, List<Object> lastObj) {
        // a[(0)]. a[(0)][(1)]
        // =>
        // a=lastObj ,
        // thisObj=a[0] ,
        // lastSplitValue= [(0)] ,
        // lastArrayIndex= (0) ,
        // thisArrayIndex= null or (1)
        // 目的是抓 thisObj,

        // 如果存在需要檢查型態
        val thisObj = getThisObjByList(lastObj, isDot, lastArrayIndex);

        // 還未結束,下次迴圈繼續做
        if (path.length() != endIndex) {
            return thisObj;
        }
        // 剛好結束,要直接做完
        // 如果是剛好結束,一定是[(n)][(n)]
        val list = CastUtils.cast(thisObj, new TypeRef<List<Object>>() {
        });
        setListValue(value, thisArrayIndex, list);
        return list;
    }

    private static void setListValue(Object value, Integer thisArrayIndex, List<Object> list) {
        if (thisArrayIndex == null) {
            list.add(value);
            return;
        }

        val thisObjListSize = list.size();
        if (thisObjListSize > thisArrayIndex) {
            list.set(thisArrayIndex, value);
            return;
        }

        val diffCount = thisArrayIndex - thisObjListSize + 1;
        IntStream.range(0, diffCount)
                .mapToObj((index) -> index == diffCount - 1 ? value : null)
                .forEach(list::add);
    }

    private static Object getThisObjByList(List<Object> lastObj, boolean isDot, Integer lastArrayIndex) {
        // a[(0)]. a[(0)][(1)]
        // =>
        // a=lastObj ,
        // thisObj=a[0] ,
        // isDot= a[(0)]. 而非 a[(0)][(1)]
        // lastSplitValue= [(0)] ,
        // lastArrayIndex= (0) ,
        // thisArrayIndex= null or (1)
        // 目的是抓 thisObj,

        // []
        if (lastArrayIndex == null) {
            // 直接放最後一個,因為我不能擅自做主
            val o = generateObj(isDot);
            lastObj.add(o);
            return o;
        }

        // [n]
        val lastObjSize = lastObj.size();
        // 可能存在
        if (lastObjSize > lastArrayIndex) {
            Object thisObj = lastObj.get(lastArrayIndex);
            if (thisObj == null) {
                lastObj.set(lastArrayIndex, thisObj = generateObj(isDot));
            } else {
                checkType(isDot, thisObj);
            }
            return thisObj;
        }
        // 不存在
        val diffCount = lastArrayIndex - lastObjSize + 1;
        IntStream.range(0, diffCount)
                .mapToObj((index) -> index == diffCount - 1 ? generateObj(isDot) : null)
                .forEach(lastObj::add);

        return lastObj.get(lastArrayIndex);
    }

    private static void checkIsList(Object o) {
        if (!(o instanceof List)) {
            throw new RuntimeException();
        }
    }

    private static void checkIsMap(Object o) {
        if (!(o instanceof Map)) {
            throw new RuntimeException();
        }
    }

    private static void checkSplitIsNotDot(String lastSplitValue) {
        if (JsonConstants.Split.DOT.equals(lastSplitValue)) {
            throw new RuntimeException();
        }
    }

    private static void checkPathEndIsNotDot(String path) {
        if (path.endsWith(JsonConstants.Split.DOT)) {
            throw new RuntimeException();
        }
    }

    private static void checkPathIsNotEmpty(String path) {
        if (path.isEmpty()) {
            throw new RuntimeException();
        }
    }

    private static void checkType(boolean isDot, Object o) {
        if (isDot) {
            checkIsMap(o);
        } else {
            checkIsList(o);
        }
    }

    private static Object generateObj(boolean isDot) {
        return isDot ?
                new HashMap<>() :
                new ArrayList<>();
    }

}