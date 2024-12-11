package com.dspread.demoui.beans;

import java.util.List;

/**
 * [一句话描述该类的功能]
 *
 * @author : [DH]
 * @createTime : [2024/12/9 17:27]
 * @updateRemark : [Get the terminal heart bean response]
 */
public class TerminalHeartBean {

    private Integer code;
    private DataDTO data;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataDTO {
        private Integer heartBeatInterval;
        private Integer upwardPositionEnable;
        private List<PushTasksDTO> pushTasks;
        private Integer inspectInterval;
        private Integer inspectStatus;

        public Integer getHeartBeatInterval() {
            return heartBeatInterval;
        }

        public void setHeartBeatInterval(Integer heartBeatInterval) {
            this.heartBeatInterval = heartBeatInterval;
        }

        public Integer getUpwardPositionEnable() {
            return upwardPositionEnable;
        }

        public void setUpwardPositionEnable(Integer upwardPositionEnable) {
            this.upwardPositionEnable = upwardPositionEnable;
        }

        public List<PushTasksDTO> getPushTasks() {
            return pushTasks;
        }

        public void setPushTasks(List<PushTasksDTO> pushTasks) {
            this.pushTasks = pushTasks;
        }

        public Integer getInspectInterval() {
            return inspectInterval;
        }

        public void setInspectInterval(Integer inspectInterval) {
            this.inspectInterval = inspectInterval;
        }

        public Integer getInspectStatus() {
            return inspectStatus;
        }

        public void setInspectStatus(Integer inspectStatus) {
            this.inspectStatus = inspectStatus;
        }

        public static class PushTasksDTO {
            private String taskId;
            private Integer taskType;
            private Integer subTaskType;
            private Integer forcePush;
            private String name;
            private Object packageName;

            public String getTaskId() {
                return taskId;
            }

            public void setTaskId(String taskId) {
                this.taskId = taskId;
            }

            public Integer getTaskType() {
                return taskType;
            }

            public void setTaskType(Integer taskType) {
                this.taskType = taskType;
            }

            public Integer getSubTaskType() {
                return subTaskType;
            }

            public void setSubTaskType(Integer subTaskType) {
                this.subTaskType = subTaskType;
            }

            public Integer getForcePush() {
                return forcePush;
            }

            public void setForcePush(Integer forcePush) {
                this.forcePush = forcePush;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Object getPackageName() {
                return packageName;
            }

            public void setPackageName(Object packageName) {
                this.packageName = packageName;
            }
        }
    }
}
