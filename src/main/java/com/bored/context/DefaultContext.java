package com.bored.context;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.bored.Bored;

public class DefaultContext extends AbstractContext {
    @Override
    public byte[] bytes() {
        assert StrUtil.isNotBlank(this.getType());
        this.addObject("type", this.getType());
        this.addObject("layout", this.getLayout());

        var template = StrUtil.isBlank(this.getType()) ? this.getLayout() :
                this.getType() + "/" + this.getLayout();

        assert StrUtil.isNotBlank(template);
        var content = Bored.jetTemplateHelper().parse(template, this.getCtx());
        return content.getBytes(CharsetUtil.CHARSET_UTF_8);
    }

}
