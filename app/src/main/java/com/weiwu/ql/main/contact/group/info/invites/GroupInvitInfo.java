package com.weiwu.ql.main.contact.group.info.invites;

import com.weiwu.ql.data.bean.CheckInvitesData;

/**
 * @author Joseph_Yan
 * on 2021/1/28
 */
class GroupInvitInfo {

    /**
     * id : 10
     * group_id : @TGS#1UECKK5GN
     * form_im_id : n1hn658b
     * to_im_id : bmjw09g6
     * status : 0
     * auditor_im_id : bmjw09g6
     * create_time : 2021年01月28日 15:45:47
     * update_time : 2021年01月28日 16:45:47
     * form_nick : 颜
     * to_nick : bmjw09g6
     */

    public String id;
    public String group_id;
    public String face_url;
    public String form_im_id;
    public String to_im_id;
    public Integer status;
    public String auditor_im_id;
    public String create_time;
    public String update_time;
    public String form_nick;
    public String to_nick;

    public String getFace_url() {
        return face_url;
    }

    public void setFace_url(String face_url) {
        this.face_url = face_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getForm_im_id() {
        return form_im_id;
    }

    public void setForm_im_id(String form_im_id) {
        this.form_im_id = form_im_id;
    }

    public String getTo_im_id() {
        return to_im_id;
    }

    public void setTo_im_id(String to_im_id) {
        this.to_im_id = to_im_id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAuditor_im_id() {
        return auditor_im_id;
    }

    public void setAuditor_im_id(String auditor_im_id) {
        this.auditor_im_id = auditor_im_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getForm_nick() {
        return form_nick;
    }

    public void setForm_nick(String form_nick) {
        this.form_nick = form_nick;
    }

    public String getTo_nick() {
        return to_nick;
    }

    public void setTo_nick(String to_nick) {
        this.to_nick = to_nick;
    }

    public GroupInvitInfo covertGroupInvites(CheckInvitesData.DataDTO data) {

        setAuditor_im_id(data.getInviteMemberId());
        setCreate_time(data.getCreatedTime());
        setFace_url(data.getInviteMemberInfo().getAvator());
        setForm_im_id(data.getFromMemberId());
        setForm_nick(data.getFromMemberInfo().getNickName());
        setGroup_id(data.getGroupId());
        setId(data.getId());
        setStatus(0);
        setTo_im_id(data.getInviteMemberId());
        setTo_nick(data.getInviteMemberInfo().getNickName());
        setUpdate_time(data.getCreatedTime());

        return this;
    }
}
