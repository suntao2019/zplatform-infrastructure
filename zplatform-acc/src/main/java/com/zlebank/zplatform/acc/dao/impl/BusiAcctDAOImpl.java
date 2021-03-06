/* 
 * BusiAcctDAOImpl.java  
 * 
 * version 1.0
 *
 * 2015年8月31日 
 * 
 * Copyright (c) 2015,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.acc.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.zlebank.zplatform.acc.bean.enums.Usage;
import com.zlebank.zplatform.acc.dao.BusiAcctDAO;
import com.zlebank.zplatform.acc.exception.BusiAcctNotExistException;
import com.zlebank.zplatform.acc.pojo.PojoBusiAcct;
import com.zlebank.zplatform.commons.dao.impl.HibernateBaseDAOImpl;

/**
 * Class Description
 *
 * @author yangying
 * @version
 * @date 2015年8月31日 下午12:07:18
 * @since
 */
@Repository
public class BusiAcctDAOImpl extends HibernateBaseDAOImpl<PojoBusiAcct>
        implements
            BusiAcctDAO {
    public PojoBusiAcct getByBusiAcctCode(String busiAcctCode){
        Criteria criteria = getSession().createCriteria(PojoBusiAcct.class);
        criteria.add(Restrictions.eq("busiAcctCode", busiAcctCode));
        PojoBusiAcct pojoBusiAcct = (PojoBusiAcct)criteria.uniqueResult(); 
        return pojoBusiAcct;
    }
    
    public long getAccount(Usage usage,String businessActorId)throws BusiAcctNotExistException{
        Criteria criteria = getSession().createCriteria(PojoBusiAcct.class);
        criteria.add(Restrictions.eq("usage", usage)).add(Restrictions.eq("businessActorId", businessActorId));
        PojoBusiAcct pojoBusiAcct = (PojoBusiAcct)criteria.uniqueResult();
        if(pojoBusiAcct == null){
            throw new BusiAcctNotExistException();
        }
        return pojoBusiAcct.getAccountId();
    }

    /**
     *
     * @param memberId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<PojoBusiAcct> getAllbusiByMid(String memberId) {
        Criteria criteria = getSession().createCriteria(PojoBusiAcct.class);
        return  criteria.add(Restrictions.eq("businessActorId", memberId)).list();
        
    }
    
    @Override
    public PojoBusiAcct getBusiCode(Usage usage,String memberId) throws BusiAcctNotExistException{
        Criteria criteria = getSession().createCriteria(PojoBusiAcct.class);
        criteria.add(Restrictions.eq("usage", usage)).add(Restrictions.eq("businessActorId", memberId));
        PojoBusiAcct pojoBusiAcct = (PojoBusiAcct)criteria.uniqueResult();
        if(pojoBusiAcct == null){
            throw new BusiAcctNotExistException();
        }
        return pojoBusiAcct;
    }
    
    @Override
    @Transactional
    public String getAccCodeByUsageAndBusiActorId(Usage usage,String businessActorId){
        String querySql = "select acct_code from T_ACC_ACCT A, T_ACC_BUSIACCT B where A.id = B.acct_id and B.usage=? and b.BUSINESS_ACTOR_ID=?";
        Query query = getSession().createSQLQuery(querySql);
        query.setParameter(0, usage.getCode());
        query.setParameter(1, businessActorId);
        String s = (String)query.uniqueResult();
        return s;
    }
}
